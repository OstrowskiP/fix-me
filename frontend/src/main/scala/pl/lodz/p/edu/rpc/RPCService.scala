package pl.lodz.p.edu.rpc

class RPCService extends MainClientRPC {
  override def push(number: Int): Unit =
    println(s"Push from server: $number")
}

       