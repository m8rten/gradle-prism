package com.m8rten.gradle.prism.query

class GraphContainer {

    Map<String, GraphAttributes> graphAttributes = new HashMap<String, GraphAttributes>()

    GraphAttributes createOrChangeFor(GraphAttributes graphAttributes) {
        if(this.graphAttributes.containsKey(graphAttributes.id)){
            this.graphAttributes.get(graphAttributes.id).name = graphAttributes.name
            this.graphAttributes.get(graphAttributes.id).type = graphAttributes.type
            this.graphAttributes.get(graphAttributes.id)
        } else {
            GraphAttributes graphToCreate = new GraphAttributes(name: graphAttributes.name, type: graphAttributes.type)
            this.graphAttributes.put(graphToCreate.id,graphToCreate)
            graphToCreate
        }
    }

    GraphAttributes graphAttributes(String id){
        graphAttributes.get(id)
    }

    List<GraphAttributes> graphAttributes(){
        graphAttributes.values().asList()
    }

    Map<Date, Integer> graphData(String id){
        graphAttributes.get(id).calculateDataFor(invocations)
    }
}
