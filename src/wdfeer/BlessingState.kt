package wdfeer

import arc.util.Time
import mindustry.Vars
import mindustry.gen.BlockUnitc
import mindustry.gen.Groups
import mindustry.gen.Player
import mindustry.world.blocks.defense.turrets.Turret.TurretBuild
import mindustry.world.blocks.production.GenericCrafter
import mindustry.world.blocks.production.GenericCrafter.GenericCrafterBuild
import mindustry.world.blocks.storage.CoreBlock.CoreBuild
import wdfeer.Blessing.*

data class BlessingState(var local: Blessing, var remote: MutableMap<Player, Blessing>)

val BlessingState.blessings: Map<Player, Blessing>
    get() = remote + (Vars.player to local)

fun BlessingState.update() {
    val deltaTicks = Time.delta
    for ((player, blessing) in blessings.filterKeys { it.isAdded }) {
        when (blessing) {
            Reimu -> Groups.build.filter { it.team == player.team() }.filterIsInstance<CoreBuild>()
                .forEach { it.healFract(0.1f * deltaTicks / 60f) }

            Nitori -> Groups.build.filter { it.team == player.team() && player.within(it, 160f) }
                .filterIsInstance<GenericCrafterBuild>()
                .forEach { it.progress += it.getProgressIncrease((it.block as GenericCrafter).craftTime) }

            Takane -> ((player.unit() as? BlockUnitc)?.tile() as? TurretBuild)?.apply {
                reloadCounter += deltaTicks
            }

            Sanae -> Groups.unit.filter { it.team == player.team() && player.within(it, 160f) }
                .forEach { it.heal(deltaTicks) }

            Aya -> player.unit().apply {
                if (isFlying) {
                    x += vel.x * deltaTicks
                    y += vel.y * deltaTicks
                }
            }

            Seija -> Groups.unit.filter { player.within(it, 140f) }
                .forEach {
                    it.x -= it.vel.x * 1.3f
                    it.y -= it.vel.y * 1.3f
                }

            Remilia -> player.bestCore().apply {
                Groups.unit.filter { it.team != player.team() && within(it, 160f) }
                    .forEach {
                        val damage = 15f * deltaTicks / 60f
                        it.damagePierce(damage)
                        this.heal(damage)
                    }
            }

            Sakuya -> Groups.bullet.filter { it.team != player.team() && player.within(it, 80f) && it.id % 3 != 0 }
                .forEach {
                    it.x = it.lastX
                    it.y = it.lastY
                }

            Byakuren -> Groups.unit.forEach {
                it.heal(10f * (if (it.team == player.team()) 2f else 1f) * deltaTicks / 60f)
            }

            else -> {}
        }
    }
}