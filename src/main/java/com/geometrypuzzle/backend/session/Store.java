package com.geometrypuzzle.backend.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.utils.Utils;
import com.geometrypuzzle.backend.workflow.Step;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
public class Store {
    @Id
    private String processKey;
    private String shapeJson;
    private Step step;
    public Store(){}
    public Shape getShape() {
        try {
            return Utils.readValue(this.shapeJson, Shape.class);
        } catch (Exception ex) {
            log.error("Error on mapping shape, {}", ex);
            log.info("Falling back on new shape");
            return new Shape();
        }
    }
    public void setShapeJson(String jsonified) {
        this.shapeJson = jsonified;
    }
    public void setShapeJson(Shape shape) {
        try {
            setShapeJson(Utils.jsonify(shape));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
