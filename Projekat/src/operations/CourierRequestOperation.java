package operations;

import com.sun.istack.internal.NotNull;
import java.util.List;

public abstract interface CourierRequestOperation
{
  public abstract boolean insertCourierRequest(@NotNull String paramString1, @NotNull String paramString2);
  
  public abstract boolean deleteCourierRequest(@NotNull String paramString);
  
  public abstract boolean changeVehicleInCourierRequest(@NotNull String paramString1, @NotNull String paramString2);
  
  public abstract List<String> getAllCourierRequests();
  
  public abstract boolean grantRequest(@NotNull String paramString);
}
