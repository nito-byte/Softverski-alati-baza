package operations;

import com.sun.istack.internal.NotNull;
import java.math.BigDecimal;
import java.util.List;

public abstract interface CourierOperations
{
  public abstract boolean insertCourier(@NotNull String paramString1, @NotNull String paramString2);
  
  public abstract boolean deleteCourier(@NotNull String paramString);
  
  public abstract List<String> getCouriersWithStatus(int paramInt);
  
  public abstract List<String> getAllCouriers();
  
  public abstract BigDecimal getAverageCourierProfit(int paramInt);
}
