# uModDetect

🌐 Language:
- 🇺🇸 English (current)
- 🇹🇷 [Türkçe](README_tr.md)

Minecraft server plugin that detects client-side mods using hidden sign-based translation packets.

## How It Works

- Monitors player connections and chat messages
- Uses hidden sign packets to detect specific mods
- Configurable mod detection with custom actions
- Sends custom messages when mods are detected

## Commands

- `/umd reload` - Reloads plugin configuration

## Permissions

- `umd.command` - Access to plugin commands (default: op)
- `umd.message` - Permission for custom messages (default: op)

## Configuration

Edit `config.yml` to configure which mods to detect and what actions to take when detected.
