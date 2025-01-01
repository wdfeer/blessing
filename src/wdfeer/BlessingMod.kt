package wdfeer

import arc.Events
import mindustry.Vars
import mindustry.game.EventType
import mindustry.mod.Mod

@Suppress("unused")
class BlessingMod : Mod() {
    val state: BlessingState = BlessingState(Blessing.None, mutableMapOf())

    init {
        initUI()

        Events.run(EventType.Trigger.update) { if (Vars.state.isGame && !Vars.state.isPaused) state.update() }

        initNet()
    }
}

enum class Blessing(val description: String) {
    None("Classic mindustry experience"),
    Reimu("Core 10% hp/s mending"),
    Sanae("Nearby units 60 hp/s healing"),
    Nitori("Nearby crafters work at 2x speed"),
    Takane("Controlled turret +100% fire rate"),
    Aya("Controlled unit +100% speed when flying"),
    Seija("Nearby enemy units move backwards"),
    Remilia("Drain 10 hp/s from each enemy unit near your core"),
    Sakuya("Some enemy projectiles are stopped before reaching you")
}
