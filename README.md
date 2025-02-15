# PostgreSQLTableLayoutOptimizer
Little tool that allow a user to generate a new DDL script with the table layout optimized in order to reduce the size of a row of the table

##The idea
The idea is to order correctly the column of your table to reduce the size of each row of the table.
Based on this article:
https://r.ena.to/blog/optimizing-postgres-table-layout-for-maximum-efficiency/
