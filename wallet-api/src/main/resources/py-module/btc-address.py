from qdbitcoin import BitcoinPrivateKey

private_key = BitcoinPrivateKey()

private = private_key.to_hex()

wif = private_key.to_wif()

public_key = private_key.public_key()

public = public_key.to_hex()

address = public_key.address()

s = private + ";" + wif + ";" + public + ";" + address

print(s)