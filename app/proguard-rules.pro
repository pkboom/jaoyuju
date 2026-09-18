# The platform recreates these by name, so their names must survive R8.
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends androidx.fragment.app.Fragment

# Record travels between activities as a Java Serializable, which reads and
# writes fields by name.
-keepnames class com.sujichim.jasanjao2.card.Record
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Room loads each database's generated _Impl class by name; WorkManager, which
# the ads SDK pulls in, has one.
-keep class * extends androidx.room.RoomDatabase { <init>(); }

# Joda-Time carries optional hooks into libraries this app does not ship.
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
