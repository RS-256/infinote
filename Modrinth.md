

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

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/BJjdFrJX_SA" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>

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


```
/infinote remove <block>
```

Removes the sound mapping for the specified block.


```
/infinote reload
```

Reloads the configuration.


```
/infinote import notebetterfabric <json>
```

Imports configuration from a **NotebetterFabric-style JSON file**  
and converts it into Infinote format.

- `<json>`: Path to the NotebetterFabric configuration file

This command allows easy migration from NotebetterFabric to Infinote.

```
/infinote transpose <from> <to> <pitchTransposer>
```

Transposes all Note Blocks in the selected cuboid area.

- `<from>`: First corner of the target area
- `<to>`: Opposite corner of the target area
- `<pitchTransposer>`: Amount of semitone shift to apply

If a transpose would push a Note Block outside the vanilla range, Infinote attempts to keep the same mapped sound by choosing another configured supporting block with a compatible pitch shift.

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

## 🔄 Migration from NotebetterFabric

Infinote provides a built-in migration command for users coming from **NotebetterFabric**.

You can import an existing NotebetterFabric configuration file directly:

```
/infinote import notebetterfabric <json>
```

- `<json>`: Path to your existing NotebetterFabric configuration file

The command reads the JSON file and converts it into Infinote format automatically.

### Why migrate?

Compared to NotebetterFabric, Infinote:

- Runs primarily on the **server side**
- Supports extended pitch ranges (with client mod installed)
- Allows floating-point semitone pitch shifting
- multiplayer compatible

After importing, use:

```
/infinote reload
```

to apply the converted configuration.

Always keep a backup of your original JSON file before importing.

Migration is designed to make switching seamless while preserving your existing sound mappings.

---

## 📜 License

This project is licensed under the **MIT License**.

You are free to:
- Use
- Modify
- Distribute
- Include in modpacks

Attribution is required.