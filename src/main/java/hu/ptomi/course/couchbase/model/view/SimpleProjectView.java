package hu.ptomi.course.couchbase.model.view;

// Throws exception now but according to the official docs this should work.
public interface SimpleProjectView {
    String getCode();
    String getName();
}
