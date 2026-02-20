# 🏋️ PAYLOADS POSTMAN - FASE 2: MÓDULO DE GIMNASIOS

## 📋 ÍNDICE
1. [Sedes (Branches)](#sedes-branches)
2. [Imágenes de Sedes](#imágenes-de-sedes)
3. [Amenidades](#amenidades)
4. [Reseñas](#reseñas)
5. [Planes de Membresía](#planes-de-membresía)

---

## 🏢 SEDES (BRANCHES)

### 1. GET /api/branches
**Descripción**: Listar todas las sedes activas (público)
**Método**: GET
**Auth**: No requiere
**Body**: N/A

---

### 2. GET /api/branches/{id}
**Descripción**: Obtener sede activa por ID (público)
**Método**: GET
**Auth**: No requiere
**URL**: `/api/branches/1`
**Body**: N/A

---

### 3. POST /api/branches
**Descripción**: Crear nueva sede (ADMIN)
**Método**: POST
**Auth**: Bearer Token (ADMIN)
**Body**:
```json
{
  "name": "CabrejoGym Chapinero",
  "city": "Bogotá",
  "address": "Calle 63 #10-50, Chapinero",
  "phone": "+573001234567",
  "email": "chapinero@cabrejogym.com",
  "openingHours": "Lunes a Viernes: 5:00 AM - 11:00 PM, Sábados: 7:00 AM - 9:00 PM, Domingos: 8:00 AM - 6:00 PM",
  "description": "Sede principal en el corazón de Chapinero. Equipamiento de última generación, zona de CrossFit, piscina semi-olímpica y sauna.",
  "latitude": 4.6533,
  "longitude": -74.0631,
  "capacity": 250,
  "areaSqm": 1500
}
```

**Ejemplo 2**:
```json
{
  "name": "CabrejoGym Unicentro",
  "city": "Bogotá",
  "address": "Av. 15 #123-30, Centro Comercial Unicentro",
  "phone": "+573009876543",
  "email": "unicentro@cabrejogym.com",
  "openingHours": "Lunes a Domingo: 6:00 AM - 10:00 PM",
  "description": "Gimnasio moderno ubicado en Unicentro. Clases grupales, zona funcional y área de spinning.",
  "latitude": 4.6597,
  "longitude": -74.0559,
  "capacity": 180,
  "areaSqm": 1200
}
```

**Ejemplo 3**:
```json
{
  "name": "CabrejoGym Salitre",
  "city": "Bogotá",
  "address": "Calle 26 #68D-35, Salitre",
  "phone": "+573005551234",
  "email": "salitre@cabrejogym.com",
  "openingHours": "24/7",
  "description": "Gimnasio 24 horas con acceso biométrico. Zona de pesas libre, máquinas Technogym y área de cardio.",
  "latitude": 4.6486,
  "longitude": -74.1024,
  "capacity": 300,
  "areaSqm": 2000
}
```

---

### 4. PUT /api/branches/{id}
**Descripción**: Actualizar sede (ADMIN)
**Método**: PUT
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/1`
**Body**:
```json
{
  "name": "CabrejoGym Chapinero Premium",
  "city": "Bogotá",
  "address": "Calle 63 #10-50, Chapinero",
  "phone": "+573001234567",
  "email": "chapinero@cabrejogym.com",
  "openingHours": "Lunes a Domingo: 5:00 AM - 11:00 PM",
  "description": "Sede premium renovada con equipamiento de última generación, zona de CrossFit, piscina semi-olímpica, sauna y jacuzzi.",
  "latitude": 4.6533,
  "longitude": -74.0631,
  "capacity": 300,
  "areaSqm": 1800
}
```

---

### 5. PATCH /api/branches/{id}/activate
**Descripción**: Activar sede (ADMIN)
**Método**: PATCH
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/1`
**Body**: N/A

---

### 6. PATCH /api/branches/{id}/deactivate
**Descripción**: Desactivar sede (ADMIN)
**Método**: PATCH
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/1`
**Body**: N/A

---

### 7. GET /api/branches/all
**Descripción**: Listar todas las sedes (activas e inactivas) (ADMIN)
**Método**: GET
**Auth**: Bearer Token (ADMIN)
**Body**: N/A

---

### 8. GET /api/branches/admin/{id}
**Descripción**: Obtener sede por ID (incluye inactivas) (ADMIN)
**Método**: GET
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/admin/1`
**Body**: N/A

---

## 📸 IMÁGENES DE SEDES

### 9. POST /api/branches/{id}/images
**Descripción**: Agregar imagen por URL (ADMIN)
**Método**: POST
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/1/images`
**Body**:
```json
{
  "url": "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800",
  "altText": "Área de pesas libre - CabrejoGym Chapinero",
  "displayOrder": 1,
  "isPrimary": true
}
```

**Ejemplo 2**:
```json
{
  "url": "https://images.unsplash.com/photo-1571902943202-507ec2618e8f?w=800",
  "altText": "Zona de cardio con vista panorámica",
  "displayOrder": 2,
  "isPrimary": false
}
```

**Ejemplo 3**:
```json
{
  "url": "https://images.unsplash.com/photo-1540497077202-7c8a3999166f?w=800",
  "altText": "Piscina semi-olímpica climatizada",
  "displayOrder": 3,
  "isPrimary": false
}
```

**Ejemplo 4**:
```json
{
  "url": "https://images.unsplash.com/photo-1593079831268-3381b0db4a77?w=800",
  "altText": "Zona de CrossFit y entrenamiento funcional",
  "displayOrder": 4,
  "isPrimary": false
}
```

---

### 10. POST /api/branches/{id}/images/upload
**Descripción**: Subir imagen (multipart/form-data) (ADMIN)
**Método**: POST
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/1/images/upload`
**Content-Type**: multipart/form-data
**Form Data**:
- `file`: [Seleccionar archivo de imagen]
- `altText`: "Entrada principal del gimnasio"
- `displayOrder`: 5
- `isPrimary`: false

---

### 11. GET /api/branches/{id}/images
**Descripción**: Listar imágenes de una sede (público)
**Método**: GET
**Auth**: No requiere
**URL**: `/api/branches/1/images`
**Body**: N/A

---

### 12. POST /api/branches/images/{imageId}/set-primary
**Descripción**: Establecer imagen como principal (ADMIN)
**Método**: POST
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/images/2/set-primary`
**Body**: N/A

---

### 13. DELETE /api/branches/images/{imageId}
**Descripción**: Eliminar imagen (ADMIN)
**Método**: DELETE
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/images/3`
**Body**: N/A

---

## 🎯 AMENIDADES

### 14. POST /api/branches/{id}/amenities
**Descripción**: Agregar amenidad a una sede (ADMIN)
**Método**: POST
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/1/amenities`
**Body**:
```json
{
  "name": "WiFi Gratis",
  "description": "Internet de alta velocidad en todas las áreas",
  "iconName": "wifi",
  "available": true
}
```

**Ejemplo 2**:
```json
{
  "name": "Estacionamiento",
  "description": "50 espacios disponibles para miembros",
  "iconName": "parking",
  "available": true
}
```

**Ejemplo 3**:
```json
{
  "name": "Duchas y Vestidores",
  "description": "Duchas con agua caliente, lockers y área de vestidores",
  "iconName": "shower",
  "available": true
}
```

**Ejemplo 4**:
```json
{
  "name": "Cafetería",
  "description": "Snacks saludables, batidos de proteína y bebidas",
  "iconName": "coffee",
  "available": true
}
```

**Ejemplo 5**:
```json
{
  "name": "Aire Acondicionado",
  "description": "Climatización en todas las áreas de entrenamiento",
  "iconName": "ac",
  "available": true
}
```

**Ejemplo 6**:
```json
{
  "name": "Entrenadores Personales",
  "description": "Staff certificado disponible para asesoría",
  "iconName": "trainer",
  "available": true
}
```

**Ejemplo 7**:
```json
{
  "name": "Clases Grupales",
  "description": "Yoga, Spinning, Zumba, CrossFit y más",
  "iconName": "group",
  "available": true
}
```

**Ejemplo 8**:
```json
{
  "name": "Sauna y Jacuzzi",
  "description": "Área de relajación con sauna seco y jacuzzi",
  "iconName": "spa",
  "available": true
}
```

**Ejemplo 9**:
```json
{
  "name": "Zona de Cardio",
  "description": "Caminadoras, elípticas y bicicletas de última generación",
  "iconName": "cardio",
  "available": true
}
```

**Ejemplo 10**:
```json
{
  "name": "Piscina",
  "description": "Piscina semi-olímpica climatizada",
  "iconName": "pool",
  "available": true
}
```

---

### 15. GET /api/branches/{id}/amenities
**Descripción**: Listar amenidades de una sede (público)
**Método**: GET
**Auth**: No requiere
**URL**: `/api/branches/1/amenities`
**Body**: N/A

---

### 16. PUT /api/branches/amenities/{amenityId}
**Descripción**: Actualizar amenidad (ADMIN)
**Método**: PUT
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/amenities/1`
**Body**:
```json
{
  "name": "WiFi Premium",
  "description": "Internet de ultra alta velocidad (500 Mbps) en todas las áreas",
  "iconName": "wifi",
  "available": true
}
```

---

### 17. DELETE /api/branches/amenities/{amenityId}
**Descripción**: Eliminar amenidad (ADMIN)
**Método**: DELETE
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/amenities/5`
**Body**: N/A

---

## ⭐ RESEÑAS

### 18. POST /api/branches/{id}/reviews
**Descripción**: Crear reseña (requiere autenticación)
**Método**: POST
**Auth**: Bearer Token (USER o ADMIN)
**URL**: `/api/branches/1/reviews`
**Body**:
```json
{
  "rating": 5,
  "comment": "Excelente gimnasio! Las instalaciones están impecables, el personal es muy amable y el equipamiento es de primera. Totalmente recomendado."
}
```

**Ejemplo 2**:
```json
{
  "rating": 4,
  "comment": "Muy buen gimnasio, solo le falta un poco más de espacio en la zona de pesas libres en horas pico."
}
```

**Ejemplo 3**:
```json
{
  "rating": 5,
  "comment": "La mejor inversión que he hecho. Los entrenadores son profesionales y las clases grupales son increíbles."
}
```

**Ejemplo 4**:
```json
{
  "rating": 3,
  "comment": "Buen gimnasio pero el estacionamiento se llena muy rápido. Las instalaciones están bien."
}
```

**Ejemplo 5**:
```json
{
  "rating": 5,
  "comment": "Me encanta la piscina y el sauna. Perfecto para relajarse después del entrenamiento."
}
```

---

### 19. GET /api/branches/{id}/reviews
**Descripción**: Listar reseñas aprobadas (público, paginado)
**Método**: GET
**Auth**: No requiere
**URL**: `/api/branches/1/reviews?page=0&size=10`
**Body**: N/A

---

### 20. PATCH /api/branches/reviews/{reviewId}/approve
**Descripción**: Aprobar reseña (ADMIN)
**Método**: PATCH
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/reviews/1/approve`
**Body**: N/A

---

### 21. PATCH /api/branches/reviews/{reviewId}/reject
**Descripción**: Rechazar reseña (ADMIN)
**Método**: PATCH
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/reviews/2/reject`
**Body**: N/A

---

### 22. DELETE /api/branches/reviews/{reviewId}
**Descripción**: Eliminar reseña (ADMIN)
**Método**: DELETE
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/reviews/3`
**Body**: N/A

---

## 💳 PLANES DE MEMBRESÍA

### 23. POST /api/branches/{id}/membership-plans
**Descripción**: Crear plan de membresía (ADMIN)
**Método**: POST
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/1/membership-plans`
**Body**:
```json
{
  "name": "Plan Mensual",
  "description": "Acceso ilimitado por 1 mes. Incluye todas las clases grupales y uso de todas las instalaciones.",
  "price": 150000,
  "durationMonths": 1,
  "isPopular": false,
  "displayOrder": 1
}
```

**Ejemplo 2**:
```json
{
  "name": "Plan Trimestral",
  "description": "Acceso ilimitado por 3 meses. Incluye todas las clases grupales, uso de instalaciones y 1 sesión de entrenamiento personal gratis.",
  "price": 400000,
  "durationMonths": 3,
  "isPopular": true,
  "displayOrder": 2
}
```

**Ejemplo 3**:
```json
{
  "name": "Plan Semestral",
  "description": "Acceso ilimitado por 6 meses. Incluye todo lo anterior más 3 sesiones de entrenamiento personal y evaluación física mensual.",
  "price": 750000,
  "durationMonths": 6,
  "isPopular": false,
  "displayOrder": 3
}
```

**Ejemplo 4**:
```json
{
  "name": "Plan Anual Premium",
  "description": "Acceso ilimitado por 12 meses. Incluye todo lo anterior más 10 sesiones de entrenamiento personal, evaluación física mensual, acceso a eventos exclusivos y descuento del 20% en la tienda.",
  "price": 1350000,
  "durationMonths": 12,
  "isPopular": true,
  "displayOrder": 4
}
```

**Ejemplo 5**:
```json
{
  "name": "Plan Estudiante",
  "description": "Plan especial para estudiantes. Acceso ilimitado por 1 mes con descuento. Requiere carnet estudiantil vigente.",
  "price": 100000,
  "durationMonths": 1,
  "isPopular": false,
  "displayOrder": 5
}
```

---

### 24. GET /api/branches/{id}/membership-plans
**Descripción**: Listar planes activos de una sede (público)
**Método**: GET
**Auth**: No requiere
**URL**: `/api/branches/1/membership-plans`
**Body**: N/A

---

### 25. GET /api/branches/{id}/membership-plans/all
**Descripción**: Listar todos los planes (activos e inactivos) (ADMIN)
**Método**: GET
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/1/membership-plans/all`
**Body**: N/A

---

### 26. PUT /api/branches/membership-plans/{planId}
**Descripción**: Actualizar plan de membresía (ADMIN)
**Método**: PUT
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/membership-plans/2`
**Body**:
```json
{
  "name": "Plan Trimestral Premium",
  "description": "Acceso ilimitado por 3 meses. Incluye todas las clases grupales, uso de instalaciones, 2 sesiones de entrenamiento personal gratis y evaluación física inicial.",
  "price": 420000,
  "durationMonths": 3,
  "isPopular": true,
  "active": true,
  "displayOrder": 2
}
```

---

### 27. DELETE /api/branches/membership-plans/{planId}
**Descripción**: Eliminar plan de membresía (ADMIN)
**Método**: DELETE
**Auth**: Bearer Token (ADMIN)
**URL**: `/api/branches/membership-plans/5`
**Body**: N/A

---

## 📝 NOTAS IMPORTANTES

### Autenticación
- **Público**: No requiere token
- **USER**: Requiere Bearer Token de usuario autenticado
- **ADMIN**: Requiere Bearer Token con rol ADMIN

### Obtener Token
1. Registrarse: `POST /api/auth/register`
2. Login: `POST /api/auth/login`
3. Usar el token en header: `Authorization: Bearer {token}`

### Flujo Recomendado de Pruebas

1. **Crear Sedes** (ADMIN)
   - POST /api/branches (crear 2-3 sedes)

2. **Agregar Imágenes** (ADMIN)
   - POST /api/branches/{id}/images (4-5 imágenes por sede)
   - POST /api/branches/images/{imageId}/set-primary (establecer imagen principal)

3. **Agregar Amenidades** (ADMIN)
   - POST /api/branches/{id}/amenities (8-10 amenidades por sede)

4. **Crear Planes de Membresía** (ADMIN)
   - POST /api/branches/{id}/membership-plans (4-5 planes por sede)

5. **Crear Reseñas** (USER)
   - POST /api/branches/{id}/reviews (3-5 reseñas por sede)

6. **Moderar Reseñas** (ADMIN)
   - PATCH /api/branches/reviews/{reviewId}/approve

7. **Consultar Información** (PÚBLICO)
   - GET /api/branches (ver todas las sedes)
   - GET /api/branches/{id} (ver detalle con rating promedio)
   - GET /api/branches/{id}/images
   - GET /api/branches/{id}/amenities
   - GET /api/branches/{id}/reviews
   - GET /api/branches/{id}/membership-plans

### Validaciones Importantes

- **Rating**: Debe ser entre 1 y 5
- **Latitude**: Entre -90.0 y 90.0
- **Longitude**: Entre -180.0 y 180.0
- **Price**: Debe ser positivo
- **DurationMonths**: Mínimo 1 mes
- **Capacity y AreaSqm**: Deben ser positivos

### Estados de Reseñas
- `PENDING`: Reseña pendiente de moderación (default)
- `APPROVED`: Reseña aprobada (visible públicamente)
- `REJECTED`: Reseña rechazada (no visible)

---

## 🎯 ENDPOINTS TOTALES: 27

- Sedes: 8 endpoints
- Imágenes: 5 endpoints
- Amenidades: 4 endpoints
- Reseñas: 5 endpoints
- Planes de Membresía: 5 endpoints
