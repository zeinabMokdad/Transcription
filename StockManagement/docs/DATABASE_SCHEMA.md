# Database Schema Documentation

## Overview

The Stock Management application uses Room Database (SQLite) for local data persistence. The database consists of four main tables: Products, Categories, Suppliers, and Transactions.

## Database Version

- **Current Version**: 1
- **Database Name**: `stock_database`

## Entity Relationships

```
Categories (1) ----< (N) Products
Suppliers (1) ----< (N) Products
Products (1) ----< (N) Transactions
```

## Tables

### 1. Categories Table

Stores product categories with support for hierarchical organization.

**Table Name**: `categories`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | LONG | PRIMARY KEY, AUTO_INCREMENT | Unique category identifier |
| name | TEXT | NOT NULL, UNIQUE | Category name |
| parentCategoryId | LONG | NULL, FOREIGN KEY | Parent category for subcategories |
| description | TEXT | NULL | Category description |
| createdAt | LONG | NOT NULL | Timestamp of creation (milliseconds) |
| updatedAt | LONG | NOT NULL | Timestamp of last update (milliseconds) |

**Indexes**:
- Unique index on `name`

---

### 2. Suppliers Table

Stores supplier/vendor information.

**Table Name**: `suppliers`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | LONG | PRIMARY KEY, AUTO_INCREMENT | Unique supplier identifier |
| name | TEXT | NOT NULL, UNIQUE | Supplier name |
| contactPerson | TEXT | NULL | Contact person name |
| email | TEXT | NULL | Email address |
| phone | TEXT | NULL | Phone number |
| address | TEXT | NULL | Physical address |
| createdAt | LONG | NOT NULL | Timestamp of creation (milliseconds) |
| updatedAt | LONG | NOT NULL | Timestamp of last update (milliseconds) |

**Indexes**:
- Unique index on `name`

---

### 3. Products Table

Stores product information with references to categories and suppliers.

**Table Name**: `products`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | LONG | PRIMARY KEY, AUTO_INCREMENT | Unique product identifier |
| name | TEXT | NOT NULL | Product name |
| sku | TEXT | NOT NULL, UNIQUE | Stock Keeping Unit |
| barcode | TEXT | NULL | Product barcode |
| categoryId | LONG | NULL, FOREIGN KEY | Reference to categories table |
| supplierId | LONG | NULL, FOREIGN KEY | Reference to suppliers table |
| description | TEXT | NULL | Product description |
| price | REAL | NOT NULL, DEFAULT 0.0 | Product price |
| quantity | INTEGER | NOT NULL, DEFAULT 0 | Current stock quantity |
| lowStockThreshold | INTEGER | NOT NULL, DEFAULT 10 | Low stock alert threshold |
| imagePath | TEXT | NULL | Path to product image |
| createdAt | LONG | NOT NULL | Timestamp of creation |
| updatedAt | LONG | NOT NULL | Timestamp of last update |

---

### 4. Transactions Table

Stores all stock movement transactions.

**Table Name**: `transactions`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | LONG | PRIMARY KEY, AUTO_INCREMENT | Unique transaction identifier |
| productId | LONG | NOT NULL, FOREIGN KEY | Reference to products table |
| type | TEXT | NOT NULL | STOCK_IN, STOCK_OUT, ADJUSTMENT |
| quantity | INTEGER | NOT NULL | Quantity involved |
| balanceBefore | INTEGER | NOT NULL | Stock before transaction |
| balanceAfter | INTEGER | NOT NULL | Stock after transaction |
| note | TEXT | NULL | Optional note |
| timestamp | LONG | NOT NULL | Transaction timestamp |

---

For complete documentation, see the full schema file.
