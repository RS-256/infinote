

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
- `<sound>`: Any valid sound ID (not limited to built-in sounds)
- `<category>`: Sound category (e.g. master, music, record, block, etc.)
- `<pitchShift>`: Pitch shift in semitones (accepts **float**)
- `<volume>`: Volume multiplier (float)

---

```
/infinote remove <block>
```

Removes the sound mapping for the specified block.

---

```
/infinote reload
```

Reloads the configuration.

---

```
/infinote import notebetterfabric <json>
```

Imports configuration from a **NotebetterFabric-style JSON file**  
and converts it into Infinote format.

- `<json>`: Path to the NotebetterFabric configuration file

This command allows easy migration from NotebetterFabric to Infinote.

---

### 🎵 About `pitchShift`

`pitchShift` represents pitch change in **semitones**.

- `12` = +12 semitones = +1 octave
- `-12` = -12 semitones = -1 octave
- `0` = no pitch change

Unlike vanilla Note Blocks, `pitchShift`:

- Accepts **floating-point values**
- Is **not limited to 12-tone equal temperament**
- Allows microtonal adjustments (e.g. `0.5`, `-2.3`, etc.)

---

### 📏 Pitch Clamp Behavior

Minecraft internally clamps pitch values.

### Without the client mod installed

Vanilla pitch clamping applies.  
If the calculated pitch exceeds vanilla limits, it will be clamped by the client.

### With the client mod installed

Pitch clamp range is expanded to:`[0.03125, 32.0]`, witch means you can change pitch within: -5va ~ +5va

note that for full extended pitch range, client installation is needed.

---

### 🔊 About `<sound>`

The `<sound>` argument is **not restricted to vanilla sound events**.

You may use:

- Custom namespaces
- Custom paths
- Any id defined inside a resourcepack

It does **NOT** need to start with `minecraft:`.

Example valid IDs:

```
minecraft:block.note_block.harp
custom:piano
my_music:grand.piano
```
you cannot use nested namespace, like `infinote.custom:piano`, or `smoe:namespace:your_path`.

The `<sound>` can contain:

- Lowercase letters (`a-z`)
- Numbers (`0-9`)
- Underscore (`_`)
- Only one colon (`:`)
- Dot (`.`)

Format:

```
<namespace>:<path>
```

Only **one colon** is allowed.

</details>

## 🎵 Resourcepack (Vanilla Structure)

follow the standard Minecraft resourcepack structure:

<details>
<summary><strong>Click to expand resourcepack structure</strong></summary>

```
resourcepack/
└─ assets/
    └─ <namespace>/
        ├─ sounds.json
        └─ sounds/
            ├─ example_sound.ogg
            └─ smth_ur_folder/
               └─ nested/
                  └─ your_sound.ogg
            
```

Example `sounds.json`:

```json
{
  "your_sound": {
    "category": "record",
    "replace": false,
    "sounds": [
      "example_sound"
    ]
  },
  "sound_id_in_the_command": {
    "category": "record",
    "replace": false,
    "sounds": [
      "smth_ur_folder/nested/your_sound"
    ]
  }
}
```

After defining the sound in `sounds.json`, use its full ID in the command:

```
/infinote add minecraft:stone <namespace>:custom.sound block 0 3
```

In the above example, you should send command:

```
/infinote add minecraft:stone <mamespace>:your_sound records 0 3
/infinote add minecraft:white_concrete <mamespace>:sound_id_in_the_command records 0 3
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



