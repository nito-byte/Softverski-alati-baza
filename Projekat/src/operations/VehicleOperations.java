package operations;

import com.sun.istack.internal.NotNull;
import java.math.BigDecimal;
import java.util.List;

public abstract interface VehicleOperations
{
  public abstract boolean insertVehicle(@NotNull String paramString, int paramInt, BigDecimal paramBigDecimal);
  
  public abstract int deleteVehicles(@NotNull String... paramVarArgs);
  
  public abstract List<String> getAllVehichles();
  
  public abstract boolean changeFuelType(@NotNull String paramString, int paramInt);
  
  public abstract boolean changeConsumption(@NotNull String paramString, BigDecimal paramBigDecimal);
}
