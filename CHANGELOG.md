## v1.5.3

### Added

- New support version; mc26.3
- `bypassBlock`: blocks registered with `/infinote bypass <add|remove|list>` let a noteblock play even when they sit on top of it. Stored in `config/infinote.json` under `bypass`, next to `mappings`. A note that only plays because of the bypass list does not emit `note_block_play`, so Sculk Sensors and Allays keep reacting only to notes vanilla would have played.

### Fixed

- A mob head placed on a noteblock whose supporting block had a custom mapping played nothing at all. The vanilla mob head sound now plays as it should. (mc1.19.3+)
- Custom mapped sounds no longer swallow the `note_block_play` game event, so Sculk Sensors and Allays react to them again. (mc1.19+)

### Changed

