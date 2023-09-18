package operations;

import com.sun.istack.internal.NotNull;
import java.util.List;

public abstract interface CityOperations
{
  public abstract int insertCity(String paramString1, String paramString2);
  
  public abstract int deleteCity(@NotNull String... paramVarArgs);
  
  public abstract boolean deleteCity(int paramInt);
  
  public abstract List<Integer> getAllCities();
}
