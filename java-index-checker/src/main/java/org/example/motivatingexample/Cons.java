package org.example.motivatingexample;


public record Cons<T>(T head, List<T> tail) implements List<T> {
    
}
