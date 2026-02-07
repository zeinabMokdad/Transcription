# Stock Management App - User Guide

## Table of Contents

1. [Getting Started](#getting-started)
2. [Dashboard](#dashboard)
3. [Product Management](#product-management)
4. [Inventory Tracking](#inventory-tracking)
5. [Categories](#categories)
6. [Suppliers](#suppliers)
7. [Reports](#reports)
8. [Settings](#settings)
9. [Tips and Best Practices](#tips-and-best-practices)

---

## Getting Started

### First Launch

When you launch the app for the first time:
1. You'll see an empty Dashboard
2. Navigate through the bottom navigation tabs to explore features
3. Start by setting up Categories and Suppliers before adding products

### Navigation

The app uses bottom navigation with 5 main sections:
- **Dashboard**: Overview and quick stats
- **Products**: Product catalog
- **Categories**: Product categories
- **Suppliers**: Supplier management
- **Reports**: Analytics and exports

---

## Dashboard

The Dashboard provides an overview of your inventory:

### Key Metrics
- **Total Products**: Count of all products in inventory
- **Low Stock Items**: Products below threshold
- **Categories**: Total number of categories
- **Suppliers**: Total number of suppliers

### Recent Transactions
View the 10 most recent stock movements with:
- Transaction type (Stock In/Out/Adjustment)
- Quantity changed
- Balance before and after
- Timestamp and notes

### Quick Actions
Tap on any metric card to navigate to the relevant section.

---

## Product Management

### Adding a Product

1. Navigate to **Products** tab
2. Tap the **+** (floating action button)
3. Fill in the product details:
   - **Name** (required): Product name
   - **SKU** (required): Unique identifier
   - **Barcode** (optional): Scan or enter manually
   - **Description**: Product details
   - **Price** (required): Selling price
   - **Quantity** (required): Initial stock
   - **Low Stock Threshold**: Alert level (default: 10)
   - **Category**: Select from dropdown
   - **Supplier**: Select from dropdown
4. Tap **Add Product**

### Editing a Product

1. Navigate to **Products** tab
2. Tap on the product you want to edit
3. Modify the details
4. Tap **Update Product**

### Deleting a Product

1. Navigate to **Products** tab
2. Find the product in the list
3. Tap the **delete icon** (trash can)
4. Confirm deletion in the dialog

### Searching Products

1. Tap the **search icon** in the Products screen
2. Type product name, SKU, or barcode
3. Results update as you type

### Barcode Scanning

1. In Add/Edit Product screen
2. Tap the **scan icon** next to barcode field
3. Grant camera permission if prompted
4. Point camera at barcode
5. Barcode will be automatically filled

---

## Inventory Tracking

### Viewing Low Stock Items

1. Navigate to **Inventory** tab
2. See all products below their low stock threshold
3. Products are sorted by quantity (lowest first)

### Adjusting Stock

1. In the Inventory screen, tap on a product
2. Or from Products screen, select a product
3. Choose transaction type:
   - **Stock In**: Receive new inventory
   - **Stock Out**: Record sales or losses
   - **Adjustment**: Correct stock count
4. Enter quantity
5. Add optional note
6. Tap **Confirm**

### Transaction History

View complete history of stock movements:
1. Navigate to Dashboard
2. Scroll to "Recent Transactions"
3. Or generate Transaction Report from Reports tab

---

## Categories

### Adding a Category

1. Navigate to **Categories** tab
2. Tap the **+** button
3. Enter category name (required)
4. Add optional description
5. Tap **Add**

### Category Hierarchy

- Categories can have subcategories (parent-child relationship)
- Set parent category when creating a subcategory
- View root categories and their subcategories

### Editing Categories

1. Find the category in the list
2. Tap on it to edit
3. Update details
4. Save changes

### Deleting Categories

1. Tap the delete icon on a category
2. Confirm deletion
3. Note: Products in deleted categories will have category set to null

---

## Suppliers

### Adding a Supplier

1. Navigate to **Suppliers** tab
2. Tap the **+** button
3. Enter supplier information:
   - **Name** (required)
   - **Contact Person**
   - **Email**
   - **Phone**
   - **Address**
4. Tap **Add**

### Managing Suppliers

- View all suppliers in a list
- Search by name or contact person
- Edit supplier details by tapping on them
- Delete suppliers using the delete icon

---

## Reports

The Reports section provides analytics and export capabilities.

### Available Reports

1. **Stock Summary Report**
   - Complete inventory overview
   - Current stock levels
   - Product values

2. **Low Stock Report**
   - Products below minimum threshold
   - Priority restocking list

3. **Transaction History**
   - All stock movements
   - Filterable by date range
   - Transaction types and quantities

4. **Category Analysis**
   - Stock distribution by category
   - Category performance metrics

5. **Supplier Report**
   - Products grouped by supplier
   - Supplier inventory levels

### Generating Reports

1. Navigate to **Reports** tab
2. Select report type
3. Choose export format:
   - **PDF**: Formatted document
   - **CSV**: Spreadsheet format
4. Report is saved to device storage
5. Share or open with other apps

### Report Filters

Some reports support filters:
- Date range
- Category selection
- Supplier selection
- Product status

---

## Settings

### Theme Settings

1. Navigate to **Settings** tab
2. Toggle **Dark Theme** switch
3. App follows system theme by default
4. Choose between Light, Dark, or System default

### Notifications

1. **Enable Notifications**: Master toggle
2. **Low Stock Alerts**: Get notified when stock is low
3. Configure alert threshold in product settings

### Data Management

#### Backup Database

1. Tap **Backup Database**
2. Choose backup location
3. Backup includes all data (products, categories, suppliers, transactions)
4. Recommended: Regular backups (weekly)

#### Restore Database

1. Tap **Restore Database**
2. Select backup file
3. Confirm restoration
4. Warning: Current data will be replaced

#### Export All Data

1. Tap **Export All Data**
2. Choose format (JSON/CSV)
3. Data is exported to downloads folder
4. Use for migration or external analysis

---

## Tips and Best Practices

### Inventory Management

1. **Set Realistic Thresholds**: Configure low stock alerts based on sales velocity
2. **Regular Audits**: Periodically verify physical stock matches app data
3. **Use Notes**: Add notes to transactions for better tracking
4. **SKU System**: Develop a consistent SKU naming convention

### Organization

1. **Category Structure**: Create logical category hierarchy
2. **Supplier Details**: Keep supplier contact info up-to-date
3. **Product Descriptions**: Add detailed descriptions for better searchability

### Data Management

1. **Regular Backups**: Backup database weekly or before major changes
2. **Export Reports**: Generate monthly reports for records
3. **Clean Data**: Remove discontinued products periodically
4. **Update Prices**: Keep product prices current

### Performance

1. **Search**: Use search feature for quick product lookup
2. **Filters**: Use category/supplier filters to narrow results
3. **Barcode**: Use barcode scanning for faster product entry
4. **Batch Operations**: Add multiple products at once when restocking

### Security

1. **Device Security**: Use device lock screen protection
2. **Backup Security**: Store backups in secure location
3. **Access Control**: Consider device sharing implications
4. **Data Privacy**: Keep supplier and product information confidential

---

## Troubleshooting

### Common Issues

**Q: Products not showing in list**
- A: Check if search filter is active, clear search to see all products

**Q: Cannot scan barcode**
- A: Ensure camera permission is granted, check device camera functionality

**Q: Low stock alerts not working**
- A: Verify notifications are enabled in Settings

**Q: Export/Backup fails**
- A: Check storage permissions, ensure sufficient storage space

**Q: App is slow**
- A: Large database may affect performance, consider archiving old transactions

### Getting Help

For additional support:
- Check app settings for version and info
- Refer to README.md for technical details
- Contact app developer for assistance

---

## Keyboard Shortcuts (Future Feature)

- Ctrl + F: Quick search
- Ctrl + N: New product
- Ctrl + S: Save current form
- Ctrl + R: Refresh data

---

## Appendix

### Glossary

- **SKU**: Stock Keeping Unit - Unique product identifier
- **Transaction**: Any change to stock quantity
- **Low Stock Threshold**: Minimum quantity before alert
- **Stock In**: Adding inventory
- **Stock Out**: Removing inventory (sales, damage, etc.)
- **Adjustment**: Manual stock correction

### Support

For questions or issues:
- Email: support@example.com
- GitHub: Open an issue
- Documentation: Check README.md

---

Last Updated: February 2026
Version: 1.0
