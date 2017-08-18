import groovy.json.JsonBuilder
def writer = new StringWriter()
def listOfNodes = []
def listOfEdges = []
def data = ["elements" : ["nodes" : [], "edges": []],
            "style": [["selector":"edge",
                       "style": ["label": "data(name)"]]
                     ]
           ]
JsonBuilder builder = new JsonBuilder(data)
builder.writeTo(writer)
writer.toString()