import sys
from qdbitcoin import send_to_address
from qdbitcoin import BitcoindClient

# for argv[0] is the file name
recipient_address = sys.argv[1]
satoshi = sys.argv[2]
satoshi = long(satoshi)
private_key = sys.argv[3]

blockchain_info = BitcoindClient("zk", "zk", False, "47.104.203.101")

signed_tx = send_to_address(recipient_address, satoshi, private_key, blockchain_info)

print(signed_tx)
