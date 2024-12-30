package wdfeer

import arc.Events
import arc.util.Time
import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.Vars
import mindustry.game.EventType
import mindustry.net.Net
import mindustry.net.NetConnection
import mindustry.net.Packet

fun BlessingMod.initNet() {
    Net.registerPacket(::BlessingPacket)
    Vars.net.handleServer(BlessingPacket::class.java) { connection: NetConnection, packet: BlessingPacket ->
        state.remote[connection.player] = packet.blessing!!
    }
    Events.on(EventType.ClientServerConnectEvent::class.java) {
        Time.run(10f) {
            Vars.net.send(BlessingPacket(state.local), true)
        }
    }
}

class BlessingPacket(var blessing: Blessing? = null) : Packet() {
    override fun read(read: Reads) {
        blessing = Blessing.entries[read.i()]
    }

    override fun write(write: Writes) {
        write.i(blessing!!.ordinal)
    }
}