package hx.nine.eleven.file.io;

@FunctionalInterface
public interface ProtocolResolver {

    Resource resolve(String var1, ResourceLoader var2);
}
