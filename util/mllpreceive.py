import socket
import ssl
import threading

# MLLP framing characters
START_BLOCK = b'\x0b'        # <VT>
END_BLOCK = b'\x1c'          # <FS>
CARRIAGE_RETURN = b'\x0d'    # <CR>

def handle_client(connstream, addr):
    try:
        buffer = b''
        while True:
            data = connstream.recv(1024)
            if not data:
                break
            buffer += data
            if END_BLOCK in buffer:
                # Unframe the message
                start = buffer.find(START_BLOCK) + 1
                end = buffer.find(END_BLOCK)
                raw_msg = buffer[start:end]
                hl7_msg = raw_msg.decode('utf-8')
                print(f"Received from {addr}:")
                lines = hl7_msg.split('\r')
                for line in lines:
                    print(line + '\n')
                
                # Send back a basic ACK
                ack = "MSH|^~\\&|ACK|RECV|SEND|FAC|20250430000000||ACK^A01|123456|P|2.3\rMSA|AA|MSG00001\r"
                print ("Sending ack with mllp framing:")
                print ("(Note that ACK reports MSG00001 as msg ctrl id")
                print ("and will fail at sender if that's not the msg ctrl id")
                print (ack)
                framed_ack = START_BLOCK + ack.encode('utf-8') + END_BLOCK + CARRIAGE_RETURN
                connstream.sendall(framed_ack)
                break
    except Exception as e:
        print(f"Error with client {addr}: {e}")
    finally:
        connstream.close()

def start_secure_mllp_server(host='0.0.0.0', port=15678, certfile='pythonserver.crt', keyfile='pythonkey.pem'):
    # Create SSL context
    context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
    #context = ssl._create_unverified_context()
    context.check_hostname = False
    context.verify_mode = ssl.CERT_NONE
    context.load_cert_chain(certfile=certfile, keyfile=keyfile)

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server_socket:
        server_socket.bind((host, port))
        server_socket.listen(5)
        print(f"Secure MLLP server listening on {host}:{port}")

        while True:
            client_socket, addr = server_socket.accept()
            connstream = context.wrap_socket(client_socket, server_side=True)
            threading.Thread(target=handle_client, args=(connstream, addr), daemon=True).start()

# Start the server
if __name__ == "__main__":
    start_secure_mllp_server()
