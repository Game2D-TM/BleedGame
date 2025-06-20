# Notes

A fully functional 2D RPG game engine developed 99% solo using pure Java (AWT) without third-party libraries or game engines.

The system was architected from scratch with a modular, object-oriented approach, allowing for future extensibility and reusability. All core systems—game loop, rendering, enemy AI, item drops, player skills, data-driven level design, quest & dialogue systems—were implemented by hand with a strong focus on clarity, separation of concerns, and performance.

Although UI is minimal, the project emphasizes robust gameplay logic, maintainable architecture, and dynamic content loading.

🔒 This game was designed and implemented almost entirely by me (Ton Minh) as a solo project. All logic, architecture, and systems were built from scratch in pure Java.

🔧 Forked from team repo. This fork represents my individual development of the project, covering 99% of the implementation. (Ton Minh)

# 🩸 BleedGame – 2D Java Action RPG (No Engine)

**BleedGame** is a self-built 2D RPG framework coded from scratch in pure Java, featuring a custom game loop, modular systems, and data-driven world design. This solo project showcases game architecture, gameplay logic, and scalable system design without using any third-party engines or libraries.

## 🔥 Highlights

- ⚙️ Frame-based Game Loop (delta time)
- 🧭 Data-Driven Level System (tilemap, enemies, quests, traps, dialogues…)
- ⚔️ Custom Physics & Collision Detection
- 🧠 Enemy AI (melee, ranged, boss) + Loot Behavior
- 💥 Visual Effects (fireballs, death fades, trap triggers)
- 🧪 Status Effects (e.g., bleed from traps)
- 🧃 Potion Shop + Inventory System
- 🔄 Skill & Cooldown System (player & enemy)
- 🎮 Player Mechanics: Movement, Jump, Double Jump, Wall Slide
- 🗺️ Minimap, Dialogue, NPC Interaction
- 🧱 Object System: touchable, untouchable, background
- 🔊 Audio Settings Menu
- 🧩 Scalable, maintainable architecture

## 📷 Demo

> Youtube : https://www.youtube.com/watch?v=y_7deiL3M6I

## 🎮 Controls (default)

| Action         | Key     |
|----------------|---------|
| Move           | WASD |
| Jump / Double Jump | W |
| Wall Slide     | Hold against wall |
| Attack         | J,K |
| Use Skill      | 1, 2, 3|
| Inventory      | I |
| Interact       | Space, Arrows, Enter |
| Open Settings  | ESC |

## 🧱 Architecture

- Modular and maintainable design using inheritance and clean naming
- Systems loosely coupled: AI, Skills, FX, Physics, Input, Inventory...
- Fully data-driven level config from external files
- Easy to expand with new levels, enemies, items, quests

## 🚧 How to Run

### Requirements

- Java 8+
- IDE (NetBeans)

### Run

```bash
git clone https://github.com/Game2D-TM/BleedGame.git
cd BleedGame
Open project in your IDE
Run Game.java

