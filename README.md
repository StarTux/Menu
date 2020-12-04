# Menu

Chest Menus for Minecraft.

Chest menus are created based on configuration files. It determines
which items to show and which actions to perform.

The point is to provide players with a visual interface in addition to
the command interface. Visual interfaces should be more explorable and
provide greater discoverability of features than textual
interfaces. Not to mention the relative calmness of looking at chest
contents compared to reading a constantly scrolling chat.

## Commands

- `/menu` Open the main menu
- `/menu <name>` Open a specific menu

Ideally players never need to open a specifically named menu, but why
not keep the option open.

## Configurations

Each menu gets one file. All menu files are stored in
`plugins/Menu/menus`. Each file is to be named after the menu's `id`
with a `.yml` suffix. The file structure is laid out by the classes in
the `config` package, with `Menu` at the top of the hierarchy.

Each menu has a title and size and contains a number of slots. Each
slot contains an item at a specific index, and some actions to perform
when it is clicked. Possible actions include commands and linkage to
other menus.

Items come with a full visual description as well as a hover text,
called tooltip. The first line of the tooltip is the item display
name.