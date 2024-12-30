package wdfeer

import arc.Events
import mindustry.Vars
import mindustry.game.EventType
import mindustry.net.Net
import mindustry.net.NetConnection
import mindustry.net.Packet

fun BlessingMod.initNet() {
    Net.registerPacket { BlessingPacket(Blessing.None) }
    Vars.net.handleServer(BlessingPacket::class.java) { connection: NetConnection, packet: BlessingPacket ->
        state.blessings[connection] = packet.blessing
    }
    Events.on(EventType.ClientServerConnectEvent::class.java) {
        Vars.net.send(BlessingPacket(state.blessings[null]!!), true)
    }
}

data class BlessingPacket(val blessing: Blessing) : Packet()