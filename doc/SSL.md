Please note that for my convenience in testing against
local certificates, certificate validation is disabled.

The util folder has mllpsend.py and mllpreceive.py, 
along with the necessary certificate and key, and the
python server's certificate has been added to truststore.jks
in src/main/resources.

To run the python script mllpsend.py, install python3
and give the command line: python3 mllpsend.py

To run the python script mllpreceive.py, install python3
and give the command line: python3 mllpreceive.py.  

These scripts are configured to send to port 15679
and receive on 15678.  The mllp forwarding example
Camel route receives on 15679 and sends to 15678.  
