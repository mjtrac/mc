# Send SSL secured message to ssl forwarding channel in mc
import socket
import ssl

START_BLOCK = b'\x0b'  # VT
END_BLOCK = b'\x1c'    # FS
CARRIAGE_RETURN = b'\x0d'

def send_secure_hl7_message(host, port, message, ca_cert="../test.crt"):
    context = ssl.create_default_context()
    context.check_hostname = False;
    context.verify_mode = ssl.CERT_NONE
    #if ca_cert:
    #    context.load_verify_locations(cafile="../test.crt")

    with socket.create_connection((host, port)) as sock:
        with context.wrap_socket(sock, server_hostname=host) as ssl_sock:
            framed = START_BLOCK + message.encode('utf-8') + END_BLOCK + CARRIAGE_RETURN
            ssl_sock.sendall(framed)
            response = ssl_sock.recv(4096)
            return response.decode('utf-8')

# Example usage
hl7_message = "MSH|^~\\&|SENDAPP|SENDFAC|RECVAPP|RECVFAC|20250430000000||ADT^A01|123456|P|2.3\rPID|1||12345^^^HOSP^MR||Doe^John\r"
ack = send_secure_hl7_message('localhost', 15679, hl7_message, ca_cert='../test.crt')
print(ack)
