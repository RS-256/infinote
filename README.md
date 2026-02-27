

# Infinote

**Server-side configurable Noteblock expansion mod. Noteblock should be configurable!**

---
## 📖 Overview

**Infinote** is a **server-side** Note Block expansion mod for Fabric.

It allows you to add custom noteblock sounds with mostly unlimited pitch shifting and fully customizable volume control.

Unlike traditional client-side mods such as Notebetter, Infinote handles sound logic on the **server side**, making it especially suitable for collaboration.

Because the server manages sound, it operates stably in multiplayer.

### Client Requirement

- If the client **does not** install the mod:
  - The mod still works.
  - Custom sounds function as long as a valid resource pack is provided.
  - However, pitch shifting may be limited by vanilla clamp behavior.

- If the client **does** install the mod:
  - Pitch clamp is expanded.
  - Extended pitch ranges function properly.
  - Full functionality is enabled.

For sounds that do not exist in vanilla Minecraft, additional resources must be provided via a resourcepack — otherwise the sound will be muted.

By using a **server-side resource pack**, all clients can hear the same custom sounds without needing to install resourcepacks manually.

---

## ⭐ Features

- Fully configurable noteblock sounds
- Block-under-based sound expansion
- Config-driven
- Primarily server-side logic
- Multiplayer support
- Multi-version ready

---

## 📸 Demonstration

https://youtu.be/your-video-id

---

## 🛠️ Usage

<details>
<summary><strong>Click to expand command usage</strong></summary>

### Commands

```
/infinote add <block> <sound> <category> <pitchShift> <volume>
```

Adds or updates a sound mapping for the specified block.

- `<block>`: Target block ID (e.g. minecraft:stone)
- `<sound>`: **Any valid sound ID** (not limited to built-in sounds; see below)
- `<category>`: Sound category (e.g. master, music, record, block, etc.)
- `<pitchShift>`: Pitch multiplier (e.g. 1.0)
- `<volume>`: Volume multiplier (e.g. 1.0)

### About `<sound>`

The `<sound>` argument is **not restricted to vanilla or built-in sound events**.

You can use:

- Custom namespaces
- Custom paths
- Any ID defined inside a resource pack

It does **NOT** need to start with `minecraft:`.

Example valid IDs:

```
minecraft:block.note_block.harp
custom:piano
my_music:grand.piano
```

### Allowed Characters

The `<sound>` ID may contain:

- Lowercase letters (`a-z`)
- Numbers (`0-9`)
- Underscore (`_`)
- One colon (`:`)
- Dot (`.`)

Format:

```
<namespace>:<path>
```

Only **one colon** is allowed.

---

```
/infinote remove <block>
```

Removes the sound mapping for the specified block.

```
/infinote reload
```

Reloads the configuration.

</details>

## 🎵 Resource Pack (Vanilla Structure)

Custom sounds must follow the standard Minecraft resourcepack structure:

<details>
<summary><strong>Click to expand resourcepack structure</strong></summary>

```
resourcepack/
└── assets/
    └── <namespace>/
        ├── sounds.json
        └── sounds/
            └── example_sound.ogg
```

Example `sounds.json`:

```json
{
  "custom.sound": {
    "sounds": [
      {
        "name": "<namespace>:example_sound",
        "stream": false
      }
    ]
  }
}
```

After defining the sound in `sounds.json`, use its full ID in the command:

```
/infinote add minecraft:stone <namespace>:custom.sound block 0 3.0
```

</details>

---

## 📜 License

This project is licensed under the **MIT License**.

You are free to:
- Use
- Modify
- Distribute
- Include in modpacks

Attribution is required.

---

# Stonecutter Fabric template

This mod uses Stonecutter template.

## Usage
- Use `"Set active project to ..."` Gradle tasks to update the Minecraft version
  available in `src/` classes.
- Use `buildAndCollect` Gradle task to store mod releases in `build/libs/`.
- Enable `mod-publish-plugin` in `stonecutter.gradle.kts` and `build.gradle.kts`
  and the corresponding code blocks to publish releases to Modrinth and Curseforge.
- Enable `maven-publish` in `build.gradle.kts` and the corresponding code block
  to publish releases to a personal maven repository.

## Useful links
- [Stonecutter beginner's guide](https://stonecutter.kikugie.dev/wiki/start/): *spoiler: you* ***need*** *to understand how it works!*
- [Fabric Discord server](https://discord.gg/v6v4pMv): for mod development help.
- [Stonecutter Discord server](https://discord.kikugie.dev/): for Stonecutter and Gradle help.
- [How To Ask Questions - the guide](http://www.catb.org/esr/faqs/smart-questions.html): also in [video form](https://www.youtube.com/results?search_query=How+To+Ask+Questions+The+Smart+Way).



