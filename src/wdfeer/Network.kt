package wdfeer

import arc.Events
import mindustry.Vars
import mindustry.game.EventType
import mindustry.gen.Groups
import mindustry.net.Net
import mindustry.net.NetConnection
import mindustry.net.Packet

fun BlessingMod.initNet() {
    Net.registerPacket { BlessingPacket(Blessing.None) }
    Vars.net.handleServer(BlessingPacket::class.java) { connection: NetConnection, packet: BlessingPacket ->
        state.remote[connection.player] = packet.blessing
    }
    Events.on(EventType.ClientServerConnectEvent::class.java) {
        Vars.net.send(BlessingPacket(state.remote[Groups.player.first()]!!), true)
    }
}

data class BlessingPacket(val blessing: Blessing) : Packet()