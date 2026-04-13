# uModDetect

Minecraft sunucu eklentisi - istemci tarafındaki modları gizli tabela paketleri ile tespit eder.

## Nasıl Çalışır

- Belirli modları tespit etmek için gizli tabela paketleri kullanır
- Yapılandırılabilir mod tespiti ve özel eylemler
- Mod tespit edildiğinde özel mesajlar gönderir

## Komutlar

- `/umd reload` - Eklenti yapılandırmasını yeniden yükler

## İzinler (Permissions)

- `umd.command` - Eklenti komutlarına erişim (varsayılan: op)
- `umd.message` - Özel mesajlar için izin (varsayılan: op)

## Yapılandırma

Tespit edilecek modları ve tespit edildiğinde yapılacak eylemleri yapılandırmak için `config.yml` dosyasını düzenleyin.
