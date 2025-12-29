Bu proje, offline-first yaklaşımıyla geliştirilmiş bir Android ToDo uygulamasıdır.
Uygulama internet olmasa bile çalışır, bağlantı geldiğinde veriler otomatik olarak senkronize edilir.

Özellikler
Room (local database) kullanımı
Firebase Firestore ile cloud senkronizasyon
Offline → Online veri senkronu
Soft delete (veri silinmez, işaretlenir)
Version & updatedTime ile conflict resolution
Task version tracking (TaskEvent tablosu)
MVVM mimarisi
Hilt (Dependency Injection)

Kullanılan Teknolojiler
Kotlin
Android Room
Firebase Firestore
Hilt
MVVM Architecture

Senkronizasyon Mantığı (Özet)
Her task bir version ve updatedTime taşır
Silme işlemi soft delete ile yapılır
Sync sırasında:
Silinen kayıt önceliklidir
Version büyük olan kazanır
Eşitse updatedTime karşılaştırılır

Amaç
Bu proje, mobil uygulamalarda backend mantığının nasıl client tarafında kurulabileceğini ve offline-first senkronizasyonun nasıl yönetileceğini göstermek için geliştirilmiştir.
