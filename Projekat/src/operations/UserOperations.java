package operations;

import com.sun.istack.internal.NotNull;
import java.util.List;

public abstract interface UserOperations
{
  public abstract boolean insertUser(@NotNull String paramString1, @NotNull String paramString2, @NotNull String paramString3, @NotNull String paramString4);
  
  public abstract int declareAdmin(@NotNull String paramString);
  
  public abstract Integer getSentPackages(@NotNull String... paramVarArgs);
  
  public abstract int deleteUsers(@NotNull String... paramVarArgs);
  
  public abstract List<String> getAllUsers();
}
