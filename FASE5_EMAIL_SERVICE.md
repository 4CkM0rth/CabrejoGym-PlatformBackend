# 📧 FASE 5.1: EMAIL SERVICE - COMPLETADO

## ✅ IMPLEMENTACIÓN COMPLETA

### 1. Dependencias Agregadas
- ✅ `spring-boot-starter-mail` - JavaMailSender
- ✅ `spring-boot-starter-thymeleaf` - Motor de plantillas HTML

### 2. Configuración
- ✅ AsyncConfig - Habilita envío asíncrono con @Async
- ✅ application.yml - Configuración SMTP

### 3. Servicio de Email
- ✅ EmailService (interface)
- ✅ EmailServiceImpl (implementación)
- ✅ EmailDTO (DTO para emails)

### 4. Plantillas HTML
- ✅ welcome-email.html - Email de bienvenida
- ✅ order-confirmation.html - Confirmación de orden
- ✅ password-reset.html - Recuperación de contraseña

---

## 📋 CONFIGURACIÓN REQUERIDA

### Variables de Entorno (.env)

Agrega estas variables a tu archivo `.env`:

```properties
# Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=tu-email@gmail.com
MAIL_PASSWORD=tu-app-password

# Frontend URL
FRONTEND_URL=http://localhost:3000
```

### Configuración de Gmail

Para usar Gmail como servidor SMTP:

1. **Habilitar verificación en 2 pasos**:
   - Ve a tu cuenta de Google
   - Seguridad → Verificación en 2 pasos
   - Actívala

2. **Generar contraseña de aplicación**:
   - Ve a https://myaccount.google.com/apppasswords
   - Selecciona "Correo" y "Otro (nombre personalizado)"
   - Escribe "CabrejoGym API"
   - Copia la contraseña generada (16 caracteres)
   - Úsala como `MAIL_PASSWORD`

3. **Alternativa (menos segura)**:
   - Habilitar "Acceso de aplicaciones menos seguras"
   - No recomendado para producción

### Otros Proveedores SMTP

**Outlook/Hotmail**:
```properties
MAIL_HOST=smtp-mail.outlook.com
MAIL_PORT=587
```

**Yahoo**:
```properties
MAIL_HOST=smtp.mail.yahoo.com
MAIL_PORT=587
```

**SendGrid** (recomendado para producción):
```properties
MAIL_HOST=smtp.sendgrid.net
MAIL_PORT=587
MAIL_USERNAME=apikey
MAIL_PASSWORD=tu-sendgrid-api-key
```

**Mailgun**:
```properties
MAIL_HOST=smtp.mailgun.org
MAIL_PORT=587
```

---

## 🎯 MÉTODOS DISPONIBLES

### 1. sendEmail(EmailDTO emailDTO)
Método genérico para enviar cualquier email con plantilla personalizada.

**Parámetros**:
- `to`: Email del destinatario
- `subject`: Asunto del email
- `template`: Nombre de la plantilla (sin extensión)
- `variables`: Map con variables para la plantilla

**Ejemplo**:
```java
Map<String, Object> variables = new HashMap<>();
variables.put("userName", "Juan");
variables.put("message", "Hola mundo");

EmailDTO email = new EmailDTO(
    "usuario@example.com",
    "Asunto del Email",
    "mi-plantilla",
    variables
);

emailService.sendEmail(email);
```

### 2. sendWelcomeEmail(User user)
Envía email de bienvenida al registrarse.

**Uso**:
```java
// En AuthServiceImpl después del registro
emailService.sendWelcomeEmail(user);
```

**Variables disponibles en la plantilla**:
- `userName`: Nombre del usuario
- `userEmail`: Email del usuario
- `frontendUrl`: URL del frontend

### 3. sendOrderConfirmationEmail(Order order)
Envía confirmación de orden después de la compra.

**Uso**:
```java
// En CheckoutServiceImpl después de crear la orden
emailService.sendOrderConfirmationEmail(order);
```

**Variables disponibles en la plantilla**:
- `userName`: Nombre del usuario
- `orderNumber`: Número de orden
- `orderTotal`: Total de la orden
- `orderItems`: Lista de items
- `orderDate`: Fecha de la orden
- `frontendUrl`: URL del frontend

### 4. sendPasswordResetEmail(User user, String resetToken)
Envía enlace para restablecer contraseña.

**Uso**:
```java
// En AuthServiceImpl al solicitar reset
String resetToken = generateResetToken();
emailService.sendPasswordResetEmail(user, resetToken);
```

**Variables disponibles en la plantilla**:
- `userName`: Nombre del usuario
- `resetLink`: Enlace completo para reset
- `frontendUrl`: URL del frontend

---

## 🔧 INTEGRACIÓN CON SERVICIOS EXISTENTES

### 1. AuthServiceImpl - Registro de Usuario

Agrega el envío de email de bienvenida:

```java
@Override
@Transactional
public AuthResponse register(RegisterRequest request) {
    // ... código existente de registro ...
    
    User savedUser = userRepository.save(user);
    
    // Enviar email de bienvenida
    emailService.sendWelcomeEmail(savedUser);
    
    // ... resto del código ...
}
```

### 2. CheckoutServiceImpl - Confirmación de Orden

Agrega el envío de email de confirmación:

```java
@Override
@Transactional
public OrderDTO checkout(CheckoutRequest request, String userEmail) {
    // ... código existente de checkout ...
    
    Order savedOrder = orderRepository.save(order);
    
    // Enviar email de confirmación
    emailService.sendOrderConfirmationEmail(savedOrder);
    
    return orderMapper.toDTO(savedOrder);
}
```

### 3. AuthServiceImpl - Recuperación de Contraseña

Implementa el método de reset:

```java
@Override
public void requestPasswordReset(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    
    // Generar token (válido por 1 hora)
    String resetToken = UUID.randomUUID().toString();
    user.setResetToken(resetToken);
    user.setResetTokenExpiry(Instant.now().plus(1, ChronoUnit.HOURS));
    userRepository.save(user);
    
    // Enviar email
    emailService.sendPasswordResetEmail(user, resetToken);
}
```

---

## 📝 CREAR PLANTILLAS PERSONALIZADAS

### Estructura de una Plantilla

Crea un archivo HTML en `src/main/resources/templates/`:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Mi Plantilla</title>
    <style>
        /* Estilos CSS inline */
    </style>
</head>
<body>
    <h1>Hola, <span th:text="${userName}">Usuario</span>!</h1>
    <p th:text="${message}">Mensaje</p>
    
    <!-- Condicionales -->
    <div th:if="${showButton}">
        <a th:href="${buttonLink}">Click aquí</a>
    </div>
    
    <!-- Iteraciones -->
    <ul>
        <li th:each="item : ${items}" th:text="${item}">Item</li>
    </ul>
    
    <!-- Formateo de números -->
    <p>Total: $<span th:text="${#numbers.formatDecimal(total, 0, 'COMMA', 2, 'POINT')}">0.00</span></p>
    
    <!-- Formateo de fechas -->
    <p th:text="${#temporals.format(date, 'dd/MM/yyyy')}">01/01/2026</p>
</body>
</html>
```

### Ejemplo: Email de Envío de Orden

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Orden Enviada</title>
</head>
<body>
    <h1>¡Tu orden ha sido enviada!</h1>
    <p>Hola, <span th:text="${userName}">Usuario</span>!</p>
    <p>Tu orden <strong th:text="${orderNumber}">#12345</strong> ha sido enviada.</p>
    <p>Número de seguimiento: <span th:text="${trackingNumber}">ABC123</span></p>
    <p>Fecha estimada de entrega: <span th:text="${#temporals.format(estimatedDelivery, 'dd/MM/yyyy')}">01/01/2026</span></p>
</body>
</html>
```

---

## ⚡ CARACTERÍSTICAS

### Envío Asíncrono
- Los emails se envían en segundo plano con `@Async`
- No bloquea la respuesta HTTP
- Mejora la experiencia del usuario

### Plantillas HTML
- Diseño responsive
- Estilos inline (compatible con clientes de email)
- Variables dinámicas con Thymeleaf
- Soporte para condicionales e iteraciones

### Manejo de Errores
- Logs detallados con SLF4J
- Excepciones capturadas y registradas
- No afecta el flujo principal de la aplicación

---

## 🧪 PRUEBAS

### Prueba Manual

1. **Configurar credenciales SMTP** en `.env`

2. **Registrar un usuario nuevo**:
```
POST /api/auth/register
{
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "tu-email-real@gmail.com",
  "password": "Password123!",
  "birthDate": "1990-01-01"
}
```

3. **Verificar tu bandeja de entrada**
   - Deberías recibir el email de bienvenida
   - Revisa también la carpeta de spam

4. **Crear una orden** (después de agregar productos al carrito):
```
POST /api/checkout
{
  "shippingAddressId": 1,
  "billingAddressId": 1
}
```

5. **Verificar email de confirmación de orden**

### Logs

Revisa los logs de la aplicación:
```
INFO  - Email sent successfully to: usuario@example.com
```

Si hay error:
```
ERROR - Error sending email to: usuario@example.com
```

---

## 🚀 PRÓXIMOS PASOS

### Mejoras Opcionales

1. **Cola de Emails con RabbitMQ/Kafka**
   - Para mayor escalabilidad
   - Reintentos automáticos
   - Priorización de emails

2. **Tracking de Emails**
   - Saber si el email fue abierto
   - Clicks en enlaces
   - Integración con SendGrid/Mailgun

3. **Más Plantillas**
   - Email de envío de orden
   - Email de devolución aprobada
   - Newsletter
   - Recordatorio de carrito abandonado

4. **Preferencias de Usuario**
   - Permitir desuscribirse
   - Elegir qué emails recibir
   - Frecuencia de notificaciones

5. **Internacionalización**
   - Plantillas en múltiples idiomas
   - Detección automática del idioma del usuario

---

## 📊 RESUMEN

### Archivos Creados
1. `EmailDTO.java` - DTO para emails
2. `EmailService.java` - Interface del servicio
3. `EmailServiceImpl.java` - Implementación del servicio
4. `AsyncConfig.java` - Configuración de @Async
5. `welcome-email.html` - Plantilla de bienvenida
6. `order-confirmation.html` - Plantilla de confirmación
7. `password-reset.html` - Plantilla de reset

### Archivos Modificados
1. `pom.xml` - Dependencias de mail y thymeleaf
2. `application.yml` - Configuración SMTP

### Configuración Requerida
- Variables de entorno para SMTP
- Contraseña de aplicación de Gmail (o API key de otro proveedor)

---

## ✅ CHECKLIST

- [x] Dependencias agregadas
- [x] Servicio de email implementado
- [x] Configuración SMTP
- [x] Plantilla de bienvenida
- [x] Plantilla de confirmación de orden
- [x] Plantilla de reset de contraseña
- [x] Envío asíncrono configurado
- [x] Documentación completa
- [ ] Integrar con AuthService (registro)
- [ ] Integrar con CheckoutService (orden)
- [ ] Integrar con AuthService (reset password)
- [ ] Configurar credenciales SMTP
- [ ] Probar envío de emails

---

**FASE 5.1: EMAIL SERVICE ✅ COMPLETADA**

Siguiente: FASE 5.2 - Pasarela de Pago (MercadoPago)
