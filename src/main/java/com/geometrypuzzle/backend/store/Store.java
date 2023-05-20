package com.geometrypuzzle.backend.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.utils.Utils;
import com.geometrypuzzle.backend.workflow.Workflow;
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
    private String pointJson;
    private Workflow.Step step;
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
    public Point getPoint() {
        try {
            return Utils.readValue(this.pointJson, Point.class);
        } catch (Exception ex) {
            log.error("Error on mapping point, {}", ex);
            log.info("Falling back on new shape");
            return new Point();
        }
    }
    public void setPointJson(String jsonified) {this.pointJson = jsonified; }
    public void setPointJson(Point point) {
        try {
            setPointJson(Utils.jsonify(point));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
