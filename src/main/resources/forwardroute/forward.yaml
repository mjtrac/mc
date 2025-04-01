# Many attempts to forward using camel-mllp failed,
# as did attempts to use marshal and unmarshal mllp,
# so ultimately used netty:tcp
# with manual insertion and removal of framing bytes
# via Java subclasses of Processor
# and used the body returned as the property CamelMllpAcknowledgementString.
#        - bean:  
#            beanType: "MllpProcessor"  
#            method: "callInnerMllp"  
#        - to:  
#            uri: "mllp://0?hostname=localhost&port=15681&autoAck=false" # send the response back to the original sender.  
#        - log: "Just after to: ${body}"
# Note that the exchange property CamelMllpAcknowledgementString
# must be set for the encompassing exchange to work.


- route:  
    id: encompassingMllpRoute  
    from:  
      uri: "mllp://0?hostname=localhost&port=15681&autoAck=false"  
      steps:
        - bean:
            beanType: "AddMllpFramingProcessor"
            method: "process"
        - to: "netty:tcp://0?host=localhost&port=15682&sync=true"
        - bean:
            beanType: "RemoveMllpFramingProcessor"
            method: "process"
        - setProperty:
            name: "CamelMllpAcknowledgementString"
            simple: "${body}"            

