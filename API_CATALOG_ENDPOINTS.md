# 📚 API Catalog Endpoints - CabrejoGym Platform

## 🎯 Base URL
```
http://localhost:8080
```

## 🔐 Authentication
All endpoints marked with 🔒 require JWT Bearer token in Authorization header:
```
Authorization: Bearer {your_jwt_token}
```

---

## 📁 CATEGORIES API

### Base Path: `/api/categories`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/` | 🔒 ADMIN | Create category |
| PUT | `/{id}` | 🔒 ADMIN | Update category |
| DELETE | `/{id}` | 🔒 ADMIN | Delete category |
| GET | `/{id}` | Public | Get category by ID |
| GET | `/slug/{slug}` | Public | Get category by slug |
| GET | `/` | Public | List all categories |
| GET | `/active` | Public | List active categories |
| GET | `/root` | Public | List root categories |
| GET | `/{parentId}/subcategories` | Public | List subcategories |

---

## 🏷️ BRANDS API

### Base Path: `/api/brands`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/` | 🔒 ADMIN | Create brand |
| PUT | `/{id}` | 🔒 ADMIN | Update brand |
| DELETE | `/{id}` | 🔒 ADMIN | Delete brand |
| GET | `/{id}` | Public | Get brand by ID |
| GET | `/slug/{slug}` | Public | Get brand by slug |
| GET | `/` | Public | List all brands |
| GET | `/active` | Public | List active brands |

---

## 🏷️ TAGS API

### Base Path: `/api/tags`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/` | 🔒 ADMIN | Create tag |
| DELETE | `/{id}` | 🔒 ADMIN | Delete tag |
| GET | `/{id}` | Public | Get tag by ID |
| GET | `/slug/{slug}` | Public | Get tag by slug |
| GET | `/` | Public | List all tags |

---

## 📦 PRODUCTS API

### Base Path: `/api/products`

#### Product Management

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/` | 🔒 ADMIN | Create product |
| PUT | `/{id}` | 🔒 ADMIN | Update product |
| DELETE | `/{id}` | 🔒 ADMIN | Delete product |
| GET | `/{id}` | Public | Get product by ID |
| GET | `/slug/{slug}` | Public | Get product by slug |
| GET | `/` | Public | List all products (paginated) |
| GET | `/all` | Public | List all products (no pagination) |
| PATCH | `/{id}/stock` | 🔒 ADMIN | Update stock |

#### Product Search & Filter

| Method | Endpoint | Auth | Description | Query Params |
|--------|----------|------|-------------|--------------|
| GET | `/search` | Public | Search products | `query`, `page`, `size` |
| GET | `/filter` | Public | Filter products | `categoryId`, `brandId`, `minPrice`, `maxPrice`, `status`, `inStock`, `page`, `size` |

#### Product Publication

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/{id}/publish` | 🔒 ADMIN | Publish product |
| POST | `/{id}/unpublish` | 🔒 ADMIN | Unpublish product |

#### Product Tags

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/{id}/tags/{tagId}` | 🔒 ADMIN | Add tag to product |
| DELETE | `/{id}/tags/{tagId}` | 🔒 ADMIN | Remove tag from product |

---

## 🎨 PRODUCT VARIANTS API

### Base Path: `/api/products`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/{id}/variants` | 🔒 ADMIN | Create variant |
| GET | `/{id}/variants` | Public | List product variants |
| PUT | `/variants/{variantId}` | 🔒 ADMIN | Update variant |
| DELETE | `/variants/{variantId}` | 🔒 ADMIN | Delete variant |

### Variant Attributes (Gym Products)

**Supplements:**
- `flavor`: Chocolate, Vainilla, Fresa, etc.
- `size`: 500g, 1kg, 2kg
- `format`: Polvo, Cápsulas, Líquido

**Equipment:**
- `weight`: 5kg, 10kg, 15kg, 20kg
- `color`: Negro, Rojo, Azul
- `material`: Hierro con goma, Acero inoxidable

**Accessories:**
- `size`: S, M, L, XL
- `color`: Negro, Rojo, Azul
- `material`: Neopreno, Algodón, Poliéster

---

## 🖼️ PRODUCT IMAGES API

### Base Path: `/api/products`

| Method | Endpoint | Auth | Description | Content-Type |
|--------|----------|------|-------------|--------------|
| POST | `/{id}/images` | 🔒 ADMIN | Add image by URL | `application/json` |
| POST | `/{id}/images/upload` | 🔒 ADMIN | Upload image file | `multipart/form-data` |
| GET | `/{id}/images` | Public | List product images | - |
| POST | `/images/{imageId}/set-primary` | 🔒 ADMIN | Set primary image | - |
| DELETE | `/images/{imageId}` | 🔒 ADMIN | Delete image | - |

### Image Upload Specifications
- **Allowed formats**: JPG, PNG
- **Max file size**: 10MB
- **Storage**: `uploads/products/`
- **Naming**: UUID-based unique filenames

---

## 📂 FILE SERVING API

### Base Path: `/api/files`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/{folder}/{filename}` | Public | Serve uploaded file |

**Example:**
```
GET /api/files/products/550e8400-e29b-41d4-a716-446655440000.jpg
```

---

## 🔐 AUTHENTICATION API

### Base Path: `/api/auth`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/register` | Public | Register new user |
| POST | `/login` | Public | Login user |

### User Roles
- `USER`: Regular customer
- `ADMIN`: Administrator with full access

---

## 📊 PAGINATION

All paginated endpoints support:
- `page`: Page number (default: 0)
- `size`: Items per page (default: 20)

**Response format:**
```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 5,
  "size": 20,
  "number": 0
}
```

---

## 🔍 SEARCH & FILTER EXAMPLES

### Search Products
```
GET /api/products/search?query=proteina&page=0&size=20
```
Searches in: name, description, shortDescription

### Filter Products
```
GET /api/products/filter?categoryId=4&brandId=1&minPrice=100000&maxPrice=200000&status=PUBLISHED&inStock=true&page=0&size=20
```

**Filter Parameters:**
- `categoryId`: Filter by category
- `brandId`: Filter by brand
- `minPrice`: Minimum price (in cents)
- `maxPrice`: Maximum price (in cents)
- `status`: DRAFT, PUBLISHED, ARCHIVED
- `inStock`: true/false

---

## 🎯 PUBLICATION STATUS

Products can have three statuses:

| Status | Description | Visible to Customers |
|--------|-------------|---------------------|
| DRAFT | Being prepared | ❌ No |
| PUBLISHED | Ready for sale | ✅ Yes |
| ARCHIVED | No longer available | ❌ No |

---

## 💰 PRICE FORMAT

All prices are in **cents** (Colombian Pesos):
- `159900` = $1,599.00 COP
- `89900` = $899.00 COP

---

## 🌐 SLUG GENERATION

Slugs are automatically generated from names:
- "Gold Standard Whey Protein" → `gold-standard-whey-protein`
- "Mancuernas Hexagonales" → `mancuernas-hexagonales`

Used for SEO-friendly URLs.

---

## ✅ VALIDATION RULES

### Product Creation
- `name`: Required, max 255 chars
- `description`: Required
- `price`: Required, positive
- `stock`: Required, non-negative
- `categoryId`: Optional
- `brandId`: Optional

### Variant Creation
- `sku`: Required, unique
- `variantName`: Required
- `stock`: Required, non-negative
- At least one attribute (flavor, size, color, weight, material, format)

### Image Upload
- File format: JPG or PNG only
- Max size: 10MB
- `altText`: Recommended for accessibility

---

## 🚀 COMPLETE WORKFLOW

### 1. Setup Catalog Structure
```
1. POST /api/categories (Create "Suplementos")
2. POST /api/categories (Create "Proteínas" with parentId)
3. POST /api/brands (Create "Optimum Nutrition")
4. POST /api/tags (Create "Bestseller")
```

### 2. Create Product
```
5. POST /api/products (Create "Gold Standard Whey")
```

### 3. Add Variants
```
6. POST /api/products/1/variants (Chocolate 1kg)
7. POST /api/products/1/variants (Vainilla 2kg)
8. POST /api/products/1/variants (Fresa 500g)
```

### 4. Add Images
```
9. POST /api/products/1/images/upload (Front image)
10. POST /api/products/1/images/upload (Back image)
11. POST /api/products/images/1/set-primary
```

### 5. Publish Product
```
12. POST /api/products/1/tags/1 (Add "Bestseller" tag)
13. POST /api/products/1/publish
```

### 6. Customer Browsing
```
14. GET /api/categories/root
15. GET /api/products/filter?categoryId=4&status=PUBLISHED
16. GET /api/products/slug/gold-standard-whey-protein
17. GET /api/products/1/variants
18. GET /api/products/1/images
```

---

## 📝 NOTES

1. **Database Required**: Create database before starting:
   ```sql
   CREATE DATABASE ecommerce CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **Liquibase**: Migrations run automatically on startup

3. **File Storage**: Ensure `uploads/products/` directory exists or will be created automatically

4. **JWT Expiration**: Tokens expire after 60 minutes

5. **Admin Access**: Only ADMIN role can create/update/delete catalog items

---

## 🔗 Related Documentation

- **Postman Payloads**: See `POSTMAN_CATALOG_PAYLOADS.md` for JSON examples
- **Implementation Details**: See `PHASE1_CATALOG_IMPLEMENTATION.md`
- **Diagnostic Report**: See `DIAGNOSTIC_REPORT.md`

---

✨ **Ready to use!** All endpoints are implemented and tested.
