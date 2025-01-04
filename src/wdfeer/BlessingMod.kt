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

        Events.on(EventType.WorldLoadEndEvent::class.java) { state.remote = mutableMapOf() }
        Events.run(EventType.Trigger.update) { if (Vars.state.isGame && !Vars.state.isPaused) state.update() }

        initNet()
    }
}

enum class Blessing(val description: String) {
    None("Classic mindustry experience"),
    Reimu("Your core regenerates 10% hp/s"),
    Sanae("Nearby friendly units regenerate 60 hp/s"),
    Nitori("Your nearby crafters work at 2x speed"),
    Takane("Controlled turret has +100% fire rate"),
    Aya("Controlled unit has +100% speed when flying"),
    Seija("Nearby units including allies move backwards at 30% speed"),
    Remilia("Drain 15 hp/s from each enemy unit near your core"),
    Sakuya("Some enemy projectiles stop before reaching you"),
    Byakuren("Heals all units 10 hp/s, allies twice as much")
}
