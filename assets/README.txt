Audio assets for the LibGDX project
--------------------------------------
Files:
- game_bgm.wav       Original short arcade/chiptune-style background loop (not a Touhou song)
- shoot.wav          Player bullet firing SFX
- enemy_die.wav      Enemy defeated SFX
- item_collect.wav   Item pickup SFX

Install:
1. Copy the four WAV files into your project's assets/audio/ folder.
2. In Main.java, change:
   audio.loadBgm("audio/touhou_bgm.ogg");
   to:
   audio.loadBgm("audio/game_bgm.wav");

3. Keep the three SFX paths as they are:
   audio.loadSound("shoot", "audio/shoot.wav");
   audio.loadSound("enemy_die", "audio/enemy_die.wav");
   audio.loadSound("item_collect", "audio/item_collect.wav");

These are original synthesized sounds. The BGM is not a Touhou track, so it does not qualify for the assignment's optional Touhou-song bonus.
