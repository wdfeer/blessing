package wdfeer

import arc.Events
import arc.util.Log
import arc.util.Time
import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.Vars
import mindustry.game.EventType
import mindustry.gen.Groups
import mindustry.gen.Player
import mindustry.net.Net
import mindustry.net.NetConnection
import mindustry.net.Packet

fun BlessingMod.initNet() {
    Net.registerPacket(::SingleBlessingPacket)
    Net.registerPacket(::BlessingStatePacket)

    Vars.net.handleServer(SingleBlessingPacket::class.java) { connection: NetConnection, packet: SingleBlessingPacket ->
        Log.debug("Packet from client: ${connection.player} has ${packet.blessing}'s blessing.")
        state.remote[connection.player] = packet.blessing!!
        Vars.net.send(BlessingStatePacket(state.blessings.mapKeys { it.key.id }), true)
    }

    Events.on(EventType.ClientServerConnectEvent::class.java) {
        state.remote = mutableMapOf()
        Time.run(10f) {
            Vars.net.send(SingleBlessingPacket(state.local), true)
        }
    }
    Vars.net.handleClient(BlessingStatePacket::class.java) {
        Time.run(10f) {
            val playerToBlessing: Map<Any, Blessing> = it.blessings!!.mapKeys { entry ->
                Groups.player.find { it.id == entry.key } ?: entry.key
            }
            for ((pl, bless) in playerToBlessing) {
                when (pl) {
                    is Player -> {
                        state.remote[pl] = bless
                        Log.debug("Packet from server: $pl has $bless's blessing")
                    }
                    is Int -> Log.err("Player#${pl} not found! Ignoring blessing entry.")
                }
            }
        }
    }
}

private class SingleBlessingPacket(var blessing: Blessing? = null) : Packet() {
    override fun read(read: Reads) {
        blessing = Blessing.entries[read.i()]
    }

    override fun write(write: Writes) {
        write.i(blessing!!.ordinal)
    }
}

private class BlessingStatePacket(var blessings: Map<Int, Blessing>? = null) : Packet() {
    override fun read(read: Reads) {
        val length = read.s()
        val pairs: MutableList<Pair<Int, Blessing>> = mutableListOf()
        for (i in 0 until length) {
            pairs.add(read.pair())
        }
        blessings = pairs.toMap()
    }

    private fun Reads.pair(): Pair<Int, Blessing> {
        return i() to Blessing.entries[i()]
    }

    override fun write(write: Writes) {
        write.s(blessings!!.size)
        for ((player, blessing) in blessings!!) {
            write.pair(player to blessing)
        }
    }

    private fun Writes.pair(pair: Pair<Int, Blessing>) {
        this.i(pair.first)
        this.i(pair.second.ordinal)
    }
}