package wdfeer

import arc.Core
import arc.Events
import arc.util.Time
import mindustry.Vars
import mindustry.game.EventType
import mindustry.mod.*

@Suppress("unused")
class BlessingMod : Mod() {
    val state: BlessingState = BlessingState(Blessing.None, mutableMapOf())

    init {
        initUI()

        Events.on(EventType.BlockBuildEndEvent::class.java, state::onBlockBuilt)

        initNet()

        scheduleUpdate()
    }

    var lastUpdateTime: Long = Time.nanos()
    private fun scheduleUpdate() {
        Core.app.post {
            if (Vars.state.isGame && !Vars.state.isPaused) {
                state.update((Time.nanos() - lastUpdateTime) / 60f)
                lastUpdateTime = Time.nanos()
            }
            scheduleUpdate()
        }
    }
}

enum class Blessing(val description: String) {
    None("Classic mindustry experience"),
    Reimu("Core 10% hp/s mending"),
    Nitori("Blocks +20% speed"),
    Takane("Turrets +100% speed, All built blocks -50% hp"),
    Sanae("Controlled unit 80 hp/s healing"),
    Aya("Controlled unit +100% speed")
}
