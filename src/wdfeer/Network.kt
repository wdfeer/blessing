package wdfeer

import arc.Events
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

    Vars.net.handleServer(SingleBlessingPacket::class.java) { connection: NetConnection, packet: SingleBlessingPacket ->
        state.remote[connection.player] = packet.blessing!!
        Vars.net.send(BlessingStatePacket(state.blessings), true)
    }

    Events.on(EventType.ClientServerConnectEvent::class.java) {
        Time.run(10f) {
            Vars.net.send(SingleBlessingPacket(state.local), true)
        }
    }
    Vars.net.handleClient(BlessingStatePacket::class.java) {
        state.remote += it.blessings!!
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

private class BlessingStatePacket(var blessings: Map<Player, Blessing>? = null) : Packet() {
    override fun read(read: Reads) {
        val length = read.s()
        val pairs: MutableList<Pair<Player, Blessing>> = mutableListOf()
        for (i in 0 until length) {
            pairs.add(read.pair())
        }
        blessings = pairs.toMap()
    }

    private fun Reads.pair(): Pair<Player, Blessing> {
        return Groups.player.find { it.id == i() } to Blessing.entries[i()]
    }

    override fun write(write: Writes) {
        write.s(blessings!!.size)
        for ((player, blessing) in blessings!!) {
            write.pair(player to blessing)
        }
    }

    private fun Writes.pair(pair: Pair<Player, Blessing>) {
        this.i(pair.first.id)
        this.i(pair.second.ordinal)
    }
}