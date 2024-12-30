package wdfeer

import arc.Events
import arc.util.Log
import mindustry.Vars
import mindustry.game.EventType
import mindustry.mod.Mod

@Suppress("unused")
class BlessingMod : Mod() {
    val state: BlessingState = BlessingState(Blessing.None, mutableMapOf())

    init {
        Log.level = Log.LogLevel.debug // TODO: set via gradle or sth

        initUI()

        Events.run(EventType.Trigger.update) { if (Vars.state.isGame && !Vars.state.isPaused) state.update() }

        initNet()
    }
}

enum class Blessing(val description: String) {
    None("Classic mindustry experience"),
    Reimu("Core 10% hp/s mending"),
    Sanae("Nearby units 60 hp/s healing"),
    Nitori("Crafters +10% speed"),
    Takane("Controlled turret +100% fire rate"),
    Aya("Controlled unit +100% speed when flying")
}
