# Funcionalidades de Carrito y Checkout

## Resumen de Implementación

Se han implementado todas las funcionalidades faltantes del sistema de checkout y carrito:

### ✅ Implementado

#### 1. Carrito Persistente (Usuario/Sesión) + Merge de Carritos
- **Entidades**: `Cart`, `CartItem`
- **Servicio**: `CartService`
- **Endpoints**: `/api/cart/*`
- **Características**:
  - Carrito persistente para usuarios autenticados
  - Carrito de sesión para usuarios invitados
  - Merge automático al iniciar sesión
  - CRUD completo de items del carrito

#### 2. Reservas de Stock con TTL
- **Entidad**: `StockReservation`
- **Servicio**: `StockReservationService`
- **Características**:
  - Reservas temporales con tiempo de expiración
  - Limpieza automática cada minuto (scheduler)
  - Cálculo de stock disponible considerando reservas activas
  - Prevención de sobreventa

#### 3. Direcciones (Shipping/Billing) + Validación
- **Entidad**: `Address`
- **Servicio**: `AddressService`
- **Endpoints**: `/api/addresses/*`
- **Características**:
  - Direcciones de envío y facturación
  - Validación de formato (código postal, teléfono)
  - Dirección por defecto
  - CRUD completo

#### 4. Cálculo de Totales Desglosado
- **DTO**: `OrderTotalsDTO`
- **Campos en Order**:
  - `subtotal`: Suma de productos
  - `discount`: Descuento aplicado (cupón)
  - `tax`: Impuestos (10%)
  - `shipping`: Costo de envío ($5, gratis >$50)
  - `total`: Total final
- **Endpoint**: `GET /api/checkout/totals?couponCode=XXX`

#### 5. Cupones/Promociones
- **Entidad**: `Coupon`
- **Servicio**: `CouponService`
- **Endpoints**: `/api/coupons/*`
- **Características**:
  - Tipos: PERCENTAGE, FIXED_AMOUNT
  - Validación de fechas (validFrom, validUntil)
  - Límite de uso (usageLimit, usageCount)
  - Compra mínima (minPurchase)
  - Descuento máximo (maxDiscount)
  - Activación/desactivación

#### 6. Generación de Número de Orden
- **Campo**: `orderNumber` en Order
- **Formato**: `ORD-YYYYMMDD-XXXXX`
- **Características**:
  - Único por orden
  - Incluye fecha y secuencia
  - Generado automáticamente en checkout

## Endpoints Principales

### Carrito
```
GET    /api/cart                    - Obtener carrito
POST   /api/cart/items              - Agregar producto
PUT    /api/cart/items/{id}         - Actualizar cantidad
DELETE /api/cart/items/{id}         - Eliminar item
DELETE /api/cart                    - Vaciar carrito
POST   /api/cart/merge              - Merge carrito invitado a usuario
```

### Checkout
```
GET    /api/checkout/totals         - Calcular totales (con cupón opcional)
POST   /api/checkout                - Realizar checkout
```

### Direcciones
```
GET    /api/addresses               - Listar mis direcciones
POST   /api/addresses               - Crear dirección
GET    /api/addresses/{id}          - Obtener dirección
PUT    /api/addresses/{id}          - Actualizar dirección
DELETE /api/addresses/{id}          - Eliminar dirección
PATCH  /api/addresses/{id}/default  - Marcar como predeterminada
```

### Cupones
```
POST   /api/coupons                 - Crear cupón (ADMIN)
GET    /api/coupons/{code}          - Obtener cupón
GET    /api/coupons/{code}/validate - Validar cupón
GET    /api/coupons                 - Listar cupones (ADMIN)
PATCH  /api/coupons/{id}/deactivate - Desactivar cupón (ADMIN)
```

## Flujo de Checkout

1. **Usuario agrega productos al carrito**
   ```
   POST /api/cart/items
   {
     "productId": 1,
     "quantity": 2
   }
   ```

2. **Usuario crea/selecciona direcciones**
   ```
   POST /api/addresses
   {
     "type": "SHIPPING",
     "fullName": "Juan Pérez",
     "street": "Calle Principal 123",
     ...
   }
   ```

3. **Usuario calcula totales (opcional con cupón)**
   ```
   GET /api/checkout/totals?couponCode=SUMMER2024
   ```

4. **Usuario realiza checkout**
   ```
   POST /api/checkout
   {
     "shippingAddressId": 1,
     "billingAddressId": 2,
     "couponCode": "SUMMER2024"
   }
   ```

## Configuración

### Scheduler
El scheduler de limpieza de reservas está habilitado automáticamente:
- Frecuencia: Cada 60 segundos
- Clase: `StockReservationCleanupScheduler`

### Constantes Configurables
En `CheckoutServiceImpl`:
- `TAX_RATE`: 10% (0.10)
- `SHIPPING_COST`: $5.00
- `FREE_SHIPPING_THRESHOLD`: $50.00

## Migración de Base de Datos

El script `V2__add_cart_checkout_features.sql` crea:
- Tablas: carts, cart_items, addresses, coupons, stock_reservations
- Actualiza tabla orders con nuevos campos
- Migra datos existentes

## Modelos de Datos

### Cart
- Relación con User (opcional, para usuarios autenticados)
- sessionId para usuarios invitados
- Lista de CartItems

### CartItem
- Producto y cantidad
- Pertenece a un Cart

### Address
- Tipo: SHIPPING, BILLING, BOTH
- Validación de formato
- Flag isDefault

### Coupon
- Tipo: PERCENTAGE o FIXED_AMOUNT
- Validación de fechas y límites
- Tracking de uso

### StockReservation
- Reserva temporal con TTL
- Asociada a sesión
- Flag active para control

### Order (actualizado)
- orderNumber único
- Desglose de totales
- Referencias a direcciones y cupón

## Seguridad

- Endpoints de carrito: Accesibles para usuarios autenticados y anónimos
- Endpoints de checkout: Requieren autenticación
- Endpoints de direcciones: Requieren autenticación
- Endpoints de cupones (admin): Requieren rol ADMIN
- Validación de ownership en direcciones

## Testing

Para probar las funcionalidades:

1. **Carrito de invitado**:
   - Agregar productos sin autenticación
   - Verificar persistencia por sessionId

2. **Merge de carrito**:
   - Agregar productos como invitado
   - Iniciar sesión
   - Llamar a `/api/cart/merge`
   - Verificar que items se combinan

3. **Checkout completo**:
   - Crear direcciones
   - Agregar productos al carrito
   - Aplicar cupón
   - Realizar checkout
   - Verificar orden con todos los campos

4. **Reservas de stock**:
   - Usar `StockReservationService` para reservar
   - Verificar que stock disponible disminuye
   - Esperar expiración
   - Verificar que stock se libera

## Próximos Pasos

Posibles mejoras futuras:
- Integración con pasarela de pago
- Notificaciones por email
- Historial de cupones usados por usuario
- Wishlist/favoritos
- Carrito guardado para después
- Recomendaciones de productos
