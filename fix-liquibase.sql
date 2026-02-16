-- Script para limpiar el estado de Liquibase y permitir que la migración 015 se ejecute correctamente
-- Ejecuta este script en tu base de datos MySQL antes de iniciar la aplicación

-- 1. Eliminar el registro de la migración 015 de Liquibase
DELETE FROM DATABASECHANGELOG 
WHERE ID = '015-update-products-catalog-fields';

-- 2. Verificar qué columnas ya existen en products
-- (Ejecuta esto para ver el estado actual)
-- DESCRIBE products;

-- 3. Si las columnas ya existen, elimínalas para que Liquibase pueda crearlas de nuevo
-- (Descomenta solo las que existan)

-- ALTER TABLE products DROP COLUMN IF EXISTS slug;
-- ALTER TABLE products DROP COLUMN IF EXISTS short_description;
-- ALTER TABLE products DROP COLUMN IF EXISTS category_id;
-- ALTER TABLE products DROP COLUMN IF EXISTS brand_id;
-- ALTER TABLE products DROP COLUMN IF EXISTS status;
-- ALTER TABLE products DROP COLUMN IF EXISTS has_variants;
-- ALTER TABLE products DROP COLUMN IF EXISTS created_at;
-- ALTER TABLE products DROP COLUMN IF EXISTS updated_at;

-- 4. Eliminar índices si existen
-- DROP INDEX IF EXISTS idx_products_slug ON products;
-- DROP INDEX IF EXISTS idx_products_status ON products;
-- DROP INDEX IF EXISTS idx_products_category_id ON products;
-- DROP INDEX IF EXISTS idx_products_brand_id ON products;
