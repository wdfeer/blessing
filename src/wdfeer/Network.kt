package wdfeer

import arc.Events
import mindustry.Vars
import mindustry.game.EventType
import mindustry.net.Net
import mindustry.net.NetConnection
import mindustry.net.Packet

fun BlessingsMod.initNet() {
    Net.registerPacket { BlessingPacket(Blessing.None) }
    Vars.net.handleServer(BlessingPacket::class.java) { connection: NetConnection, packet: BlessingPacket ->
        // TODO: sync client's blessing
    }
    Events.on(EventType.ClientServerConnectEvent::class.java) {
        Vars.net.send(BlessingPacket(activeBlessing), true)
    }
}

data class BlessingPacket(val blessing: Blessing) : Packet()