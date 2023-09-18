package operations;

import java.math.BigDecimal;
import java.util.List;

public abstract interface PackageOperations
{
  public abstract int insertPackage(int paramInt1, int paramInt2, @com.sun.istack.internal.NotNull String paramString, int paramInt3, BigDecimal paramBigDecimal);
  
  public abstract int insertTransportOffer(@com.sun.istack.internal.NotNull String paramString, int paramInt, BigDecimal paramBigDecimal);
  
  public abstract boolean acceptAnOffer(int paramInt);
  
  public static abstract interface Pair<A, B>
  {
    public abstract A getFirstParam();
    
    public abstract B getSecondParam();
    
    public static boolean equals(Pair a, Pair b)
    {
      return (a.getFirstParam().equals(b.getFirstParam())) && (a.getSecondParam().equals(b.getSecondParam()));
    }
  }
  
  public abstract List<Integer> getAllOffers();
  
  public abstract List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int paramInt);
  
  public abstract boolean deletePackage(int paramInt);
  
  public abstract boolean changeWeight(int paramInt, @com.sun.istack.internal.NotNull BigDecimal paramBigDecimal);
  
  public abstract boolean changeType(int paramInt1, int paramInt2);
  
  public abstract Integer getDeliveryStatus(int paramInt);
  
  public abstract BigDecimal getPriceOfDelivery(int paramInt);
  
  public abstract java.sql.Date getAcceptanceTime(int paramInt);
  
  public abstract List<Integer> getAllPackagesWithSpecificType(int paramInt);
  
  public abstract List<Integer> getAllPackages();
  
  public abstract List<Integer> getDrive(String paramString);
  
  public abstract int driveNextPackage(@com.sun.istack.internal.NotNull String paramString);
}
