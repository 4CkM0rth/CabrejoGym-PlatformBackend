# 🔍 PAYLOADS POSTMAN - BÚSQUEDA AVANZADA

## 📋 ENDPOINT PRINCIPAL

**Base URL**: `http://localhost:8080/api/products/advanced-search`

**Método**: GET

**Auth**: No requiere (público)

---

## 🎯 EJEMPLOS PARA POSTMAN

### 1. Búsqueda Simple por Texto
**URL**:
```
http://localhost:8080/api/products/advanced-search?query=proteína
```

**Descripción**: Busca "proteína" en nombre, descripción y descripción corta

---

### 2. Filtrar por Categoría
**URL**:
```
http://localhost:8080/api/products/advanced-search?categoryId=1
```

**Descripción**: Todos los productos de la categoría con ID 1

---

### 3. Filtrar por Marca
**URL**:
```
http://localhost:8080/api/products/advanced-search?brandId=2
```

**Descripción**: Todos los productos de la marca con ID 2

---

### 4. Filtrar por Rango de Precio
**URL**:
```
http://localhost:8080/api/products/advanced-search?minPrice=50000&maxPrice=200000
```

**Descripción**: Productos entre $50,000 y $200,000

---

### 5. Solo Productos con Descuento
**URL**:
```
http://localhost:8080/api/products/advanced-search?hasDiscount=true
```

**Descripción**: Solo productos que tienen descuento activo

---

### 6. Solo Productos en Stock
**URL**:
```
http://localhost:8080/api/products/advanced-search?inStock=true
```

**Descripción**: Solo productos con stock > 0

---

### 7. Filtrar por Estado
**URL**:
```
http://localhost:8080/api/products/advanced-search?status=PUBLISHED
```

**Descripción**: Solo productos publicados

**Valores válidos**: `DRAFT`, `PUBLISHED`, `ARCHIVED`

---

### 8. Filtrar por Múltiples Tags
**URL**:
```
http://localhost:8080/api/products/advanced-search?tagIds=1,2,3
```

**Descripción**: Productos que tienen los tags con ID 1, 2 o 3

---

### 9. Ordenar por Precio Ascendente
**URL**:
```
http://localhost:8080/api/products/advanced-search?sortBy=price&sortDirection=asc
```

**Descripción**: Productos ordenados de menor a mayor precio

---

### 10. Ordenar por Precio Descendente
**URL**:
```
http://localhost:8080/api/products/advanced-search?sortBy=price&sortDirection=desc
```

**Descripción**: Productos ordenados de mayor a menor precio

---

### 11. Ordenar por Nombre
**URL**:
```
http://localhost:8080/api/products/advanced-search?sortBy=name&sortDirection=asc
```

**Descripción**: Productos ordenados alfabéticamente

---

### 12. Ordenar por Descuento
**URL**:
```
http://localhost:8080/api/products/advanced-search?sortBy=discount&sortDirection=desc
```

**Descripción**: Productos con mayor descuento primero

---

### 13. Productos Más Recientes
**URL**:
```
http://localhost:8080/api/products/advanced-search?sortBy=createdAt&sortDirection=desc
```

**Descripción**: Últimos productos agregados

---

### 14. Búsqueda con Paginación
**URL**:
```
http://localhost:8080/api/products/advanced-search?query=creatina&page=0&size=10
```

**Descripción**: Primera página con 10 resultados

---

### 15. Segunda Página
**URL**:
```
http://localhost:8080/api/products/advanced-search?query=creatina&page=1&size=10
```

**Descripción**: Segunda página con 10 resultados

---

### 16. Búsqueda Combinada: Categoría + Precio + Stock
**URL**:
```
http://localhost:8080/api/products/advanced-search?categoryId=1&minPrice=30000&maxPrice=150000&inStock=true
```

**Descripción**: Productos de categoría 1, entre $30k-$150k, en stock

---

### 17. Búsqueda Combinada: Texto + Marca + Descuento
**URL**:
```
http://localhost:8080/api/products/advanced-search?query=proteína&brandId=2&hasDiscount=true
```

**Descripción**: Proteínas de marca 2 con descuento

---

### 18. Búsqueda Combinada: Categoría + Tags + Ordenamiento
**URL**:
```
http://localhost:8080/api/products/advanced-search?categoryId=1&tagIds=1,3,5&sortBy=price&sortDirection=asc
```

**Descripción**: Productos de categoría 1 con tags específicos, ordenados por precio

---

### 19. Ofertas del Día (Descuentos Ordenados)
**URL**:
```
http://localhost:8080/api/products/advanced-search?hasDiscount=true&status=PUBLISHED&sortBy=discount&sortDirection=desc&page=0&size=20
```

**Descripción**: Top 20 productos con mayor descuento

---

### 20. Productos Económicos en Stock
**URL**:
```
http://localhost:8080/api/products/advanced-search?maxPrice=50000&inStock=true&status=PUBLISHED&sortBy=price&sortDirection=asc
```

**Descripción**: Productos hasta $50k en stock, ordenados por precio

---

### 21. Búsqueda Completa (Todos los Filtros)
**URL**:
```
http://localhost:8080/api/products/advanced-search?query=suplemento&categoryId=1&brandId=2&tagIds=1,3,5&minPrice=30000&maxPrice=100000&hasDiscount=true&inStock=true&status=PUBLISHED&sortBy=price&sortDirection=asc&page=0&size=20
```

**Descripción**: Búsqueda con todos los filtros aplicados

---

## 🎨 CASOS DE USO REALES

### Caso 1: Página de Catálogo con Filtros
```
http://localhost:8080/api/products/advanced-search?categoryId=1&minPrice=50000&maxPrice=200000&inStock=true&sortBy=price&sortDirection=asc&page=0&size=20
```

**Escenario**: Usuario en la página de "Suplementos" filtrando por precio y disponibilidad

---

### Caso 2: Barra de Búsqueda del Header
```
http://localhost:8080/api/products/advanced-search?query=creatina&status=PUBLISHED&page=0&size=10
```

**Escenario**: Usuario busca "creatina" en la barra de búsqueda principal

---

### Caso 3: Sección "Ofertas" del Home
```
http://localhost:8080/api/products/advanced-search?hasDiscount=true&status=PUBLISHED&sortBy=discount&sortDirection=desc&page=0&size=12
```

**Escenario**: Mostrar 12 productos con mayor descuento en el home

---

### Caso 4: Sección "Nuevos Productos"
```
http://localhost:8080/api/products/advanced-search?status=PUBLISHED&sortBy=createdAt&sortDirection=desc&page=0&size=8
```

**Escenario**: Mostrar los 8 productos más recientes

---

### Caso 5: Página de Marca
```
http://localhost:8080/api/products/advanced-search?brandId=2&status=PUBLISHED&sortBy=name&sortDirection=asc
```

**Escenario**: Usuario viendo todos los productos de "Optimum Nutrition"

---

### Caso 6: Productos Relacionados
```
http://localhost:8080/api/products/advanced-search?categoryId=1&tagIds=3,5,7&status=PUBLISHED&page=0&size=4
```

**Escenario**: Mostrar 4 productos relacionados en la página de detalle

---

### Caso 7: Filtro "Disponibles Ahora"
```
http://localhost:8080/api/products/advanced-search?inStock=true&status=PUBLISHED&sortBy=createdAt&sortDirection=desc
```

**Escenario**: Mostrar solo productos disponibles para compra inmediata

---

### Caso 8: Productos Premium (Precio Alto)
```
http://localhost:8080/api/products/advanced-search?minPrice=200000&status=PUBLISHED&sortBy=price&sortDirection=desc
```

**Escenario**: Sección de productos premium/alta gama

---

### Caso 9: Productos Económicos
```
http://localhost:8080/api/products/advanced-search?maxPrice=50000&status=PUBLISHED&sortBy=price&sortDirection=asc
```

**Escenario**: Sección de productos económicos/accesibles

---

### Caso 10: Búsqueda por Tags (Ej: "Vegano")
```
http://localhost:8080/api/products/advanced-search?tagIds=5&status=PUBLISHED
```

**Escenario**: Usuario filtra por productos veganos

---

## 📊 RESPUESTA ESPERADA

Todos los endpoints retornan el mismo formato:

```json
{
  "content": [
    {
      "id": 1,
      "name": "Proteína Whey Gold Standard",
      "slug": "proteina-whey-gold-standard",
      "description": "Proteína de suero de leche de alta calidad...",
      "shortDescription": "Proteína premium para desarrollo muscular",
      "price": 180000.00,
      "hasDiscount": true,
      "discountPercent": 15.00,
      "stock": 50,
      "category": {
        "id": 1,
        "name": "Suplementos",
        "slug": "suplementos",
        "description": "Suplementos deportivos",
        "active": true
      },
      "brand": {
        "id": 2,
        "name": "Optimum Nutrition",
        "slug": "optimum-nutrition",
        "description": "Marca líder en suplementos",
        "logoUrl": "https://...",
        "active": true
      },
      "status": "PUBLISHED",
      "hasVariants": true,
      "images": [
        {
          "id": 1,
          "url": "https://...",
          "altText": "Proteína Whey",
          "displayOrder": 1,
          "isPrimary": true
        }
      ],
      "tags": [
        {
          "id": 1,
          "name": "Proteína",
          "slug": "proteina"
        }
      ],
      "createdAt": "2026-02-20T10:00:00Z",
      "updatedAt": "2026-02-20T10:00:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 95,
  "last": false,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 20,
  "first": true,
  "empty": false
}
```

---

## 🔧 PARÁMETROS DE ORDENAMIENTO

### sortBy (Campo de ordenamiento)
- `price` - Ordenar por precio
- `name` - Ordenar por nombre alfabéticamente
- `discount` - Ordenar por porcentaje de descuento
- `createdAt` o `created` - Ordenar por fecha de creación (default)

### sortDirection (Dirección)
- `asc` - Ascendente (menor a mayor, A-Z)
- `desc` - Descendente (mayor a menor, Z-A) - default

---

## ⚠️ NOTAS IMPORTANTES

1. **Paginación**:
   - `page` empieza en 0 (primera página = 0)
   - `size` por defecto es 20
   - `size` máximo es 100

2. **Filtros Opcionales**:
   - Todos los parámetros son opcionales
   - Si no se envía ningún filtro, retorna todos los productos

3. **Combinación de Filtros**:
   - Puedes combinar cualquier cantidad de filtros
   - Los filtros se aplican con AND (todos deben cumplirse)

4. **Tags Múltiples**:
   - `tagIds` acepta múltiples IDs separados por coma
   - Ejemplo: `tagIds=1,2,3`
   - Busca productos que tengan AL MENOS UNO de esos tags (OR)

5. **Estado por Defecto**:
   - Si no especificas `status`, retorna productos de todos los estados
   - Para catálogo público, siempre usa `status=PUBLISHED`

---

## 🧪 PRUEBAS RECOMENDADAS

### Secuencia de Pruebas:

1. ✅ **Prueba básica**: Sin parámetros
   ```
   GET /api/products/advanced-search
   ```

2. ✅ **Búsqueda por texto**: Con query
   ```
   GET /api/products/advanced-search?query=proteína
   ```

3. ✅ **Filtro simple**: Por categoría
   ```
   GET /api/products/advanced-search?categoryId=1
   ```

4. ✅ **Filtro de precio**: Rango
   ```
   GET /api/products/advanced-search?minPrice=50000&maxPrice=150000
   ```

5. ✅ **Ordenamiento**: Por precio
   ```
   GET /api/products/advanced-search?sortBy=price&sortDirection=asc
   ```

6. ✅ **Combinado**: Texto + filtros + ordenamiento
   ```
   GET /api/products/advanced-search?query=creatina&categoryId=1&inStock=true&sortBy=price&sortDirection=asc
   ```

7. ✅ **Paginación**: Múltiples páginas
   ```
   GET /api/products/advanced-search?page=0&size=5
   GET /api/products/advanced-search?page=1&size=5
   ```

8. ✅ **Todos los filtros**: Búsqueda completa
   ```
   GET /api/products/advanced-search?query=suplemento&categoryId=1&brandId=2&tagIds=1,3&minPrice=30000&maxPrice=100000&hasDiscount=true&inStock=true&status=PUBLISHED&sortBy=price&sortDirection=asc&page=0&size=10
   ```

---

## 🎯 VERIFICACIÓN DE RENDIMIENTO

Para verificar que los índices están funcionando, ejecuta en tu base de datos:

```sql
-- Ver el plan de ejecución
EXPLAIN SELECT * FROM products 
WHERE status = 'PUBLISHED' 
AND category_id = 1 
AND price BETWEEN 50000 AND 200000;

-- Verificar índices creados
SHOW INDEX FROM products WHERE Key_name LIKE 'idx_%';
```

Deberías ver que usa los índices en lugar de hacer "Full Table Scan".

---

## ✅ CHECKLIST DE PRUEBAS

- [ ] Búsqueda sin parámetros (todos los productos)
- [ ] Búsqueda por texto
- [ ] Filtro por categoría
- [ ] Filtro por marca
- [ ] Filtro por rango de precio
- [ ] Filtro por descuento
- [ ] Filtro por stock
- [ ] Filtro por estado
- [ ] Filtro por tags múltiples
- [ ] Ordenamiento por precio (asc/desc)
- [ ] Ordenamiento por nombre
- [ ] Ordenamiento por descuento
- [ ] Ordenamiento por fecha
- [ ] Paginación (página 0, 1, 2...)
- [ ] Combinación de múltiples filtros
- [ ] Búsqueda completa (todos los parámetros)

---

¡Listo para probar! 🚀
