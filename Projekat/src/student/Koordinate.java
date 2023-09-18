/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;


public class Koordinate {
    private int X1;
    private int Y1;
    private int X2;
    private int Y2;

    public Koordinate(int X1, int Y1, int X2, int Y2) {
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;
    }

    public int getX1() {
        return X1;
    }

    public void setX1(int X1) {
        this.X1 = X1;
    }

    public int getY1() {
        return Y1;
    }

    public void setY1(int Y1) {
        this.Y1 = Y1;
    }

    public int getX2() {
        return X2;
    }

    public void setX2(int X2) {
        this.X2 = X2;
    }

    public int getY2() {
        return Y2;
    }

    public void setY2(int Y2) {
        this.Y2 = Y2;
    }
    
    
}










//IF OBJECT_ID (N'dbo.ufnGetInventoryStock', N'FN') IS NOT NULL  
//    DROP FUNCTION ufnGetInventoryStock;  
//GO  
//CREATE FUNCTION dbo.ufnGetInventoryStock(@ProductID int)  
//RETURNS int   
//AS   
//-- Returns the stock level for the product.  
//BEGIN  
//    DECLARE @ret int;  
//    SELECT @ret = SUM(p.Quantity)   
//    FROM Production.ProductInventory p   
//    WHERE p.ProductID = @ProductID   
//        AND p.LocationID = '6';  
//     IF (@ret IS NULL)   
//        SET @ret = 0;  
//    RETURN @ret;  
//END;  
//GO  

//SELECT ProductModelID, Name, dbo.ufnGetInventoryStock(ProductID)AS CurrentSupply  
//FROM Production.Product  
//WHERE ProductModelID BETWEEN 75 and 80;   


//IF OBJECT_ID (N'Sales.ufn_SalesByStore', N'IF') IS NOT NULL  
//    DROP FUNCTION Sales.ufn_SalesByStore;  
//GO  
//CREATE FUNCTION Sales.ufn_SalesByStore (@storeid int)  
//RETURNS TABLE  
//AS  
//RETURN   
//(  
//    SELECT P.ProductID, P.Name, SUM(SD.LineTotal) AS 'Total'  
//    FROM Production.Product AS P   
//    JOIN Sales.SalesOrderDetail AS SD ON SD.ProductID = P.ProductID  
//    JOIN Sales.SalesOrderHeader AS SH ON SH.SalesOrderID = SD.SalesOrderID  
//    JOIN Sales.Customer AS C ON SH.CustomerID = C.CustomerID  
//    WHERE C.StoreID = @storeid  
//    GROUP BY P.ProductID, P.Name  
//);

//SELECT * FROM Sales.ufn_SalesByStore (602);  