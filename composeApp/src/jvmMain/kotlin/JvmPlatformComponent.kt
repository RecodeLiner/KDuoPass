import androidx.room.RoomDatabase
import com.rcl.kduopass.data.database.AppDatabase
import com.rcl.kduopass.di.PlatformSpecificComponents
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class JvmPlatformComponent: PlatformSpecificComponents() {
    @Provides
    fun providesDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> = getDatabaseBuilder()
}