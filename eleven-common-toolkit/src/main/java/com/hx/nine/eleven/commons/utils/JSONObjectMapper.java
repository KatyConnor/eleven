package com.hx.nine.eleven.commons.utils;

import com.hx.nine.eleven.commons.json.serializer.DataTimeDefaultDeserializer;
import com.hx.nine.eleven.commons.json.serializer.DateTimeDefaultSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.ConstructorDetector;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MutableCoercionConfig;
import com.fasterxml.jackson.databind.cfg.MutableConfigOverride;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.introspect.AccessorNamingStrategy;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Object 和 json String 互相转换
 *
 * @author wml
 * @Description
 * @data 2022-05-20
 */
public class JSONObjectMapper extends ObjectMapper{

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONObjectMapper.class);

    public JSONObjectMapper() {
        super();
    }

    public JSONObjectMapper(JsonFactory jf) {
        super(jf);
    }

    protected JSONObjectMapper(ObjectMapper src) {
        super(src);
    }

    public JSONObjectMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc) {
        super(jf, sp, dc);
    }

    @Override
    protected ClassIntrospector defaultClassIntrospector() {
        return super.defaultClassIntrospector();
    }

    @Override
    public JSONObjectMapper copy() {
        return (JSONObjectMapper)super.copy();
    }

    @Override
    protected void _checkInvalidCopy(Class<?> exp) {
        super._checkInvalidCopy(exp);
    }

    @Override
    protected ObjectReader _newReader(DeserializationConfig config) {
        return super._newReader(config);
    }

    @Override
    protected ObjectReader _newReader(DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues) {
        return super._newReader(config, valueType, valueToUpdate, schema, injectableValues);
    }

    @Override
    protected ObjectWriter _newWriter(SerializationConfig config) {
        return super._newWriter(config);
    }

    @Override
    protected ObjectWriter _newWriter(SerializationConfig config, FormatSchema schema) {
        return super._newWriter(config, schema);
    }

    @Override
    protected ObjectWriter _newWriter(SerializationConfig config, JavaType rootType, PrettyPrinter pp) {
        return super._newWriter(config, rootType, pp);
    }

    @Override
    public Version version() {
        return super.version();
    }

    @Override
    public JSONObjectMapper registerModule(Module module) {
        return (JSONObjectMapper)super.registerModule(module);
    }

    @Override
    public JSONObjectMapper registerModules(Module... modules) {
        return (JSONObjectMapper)super.registerModules(modules);
    }

    @Override
    public JSONObjectMapper registerModules(Iterable<? extends Module> modules) {
        return (JSONObjectMapper)super.registerModules(modules);
    }

    @Override
    public Set<Object> getRegisteredModuleIds() {
        return super.getRegisteredModuleIds();
    }

    @Override
    public JSONObjectMapper findAndRegisterModules() {
        return (JSONObjectMapper)super.findAndRegisterModules();
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) throws IOException {
        return super.createGenerator(out);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, JsonEncoding enc) throws IOException {
        return super.createGenerator(out, enc);
    }

    @Override
    public JsonGenerator createGenerator(Writer w) throws IOException {
        return super.createGenerator(w);
    }

    @Override
    public JsonGenerator createGenerator(File outputFile, JsonEncoding enc) throws IOException {
        return super.createGenerator(outputFile, enc);
    }

    @Override
    public JsonGenerator createGenerator(DataOutput out) throws IOException {
        return super.createGenerator(out);
    }

    @Override
    public JsonParser createParser(File src) throws IOException {
        return super.createParser(src);
    }

    @Override
    public JsonParser createParser(URL src) throws IOException {
        return super.createParser(src);
    }

    @Override
    public JsonParser createParser(InputStream in) throws IOException {
        return super.createParser(in);
    }

    @Override
    public JsonParser createParser(Reader r) throws IOException {
        return super.createParser(r);
    }

    @Override
    public JsonParser createParser(byte[] content) throws IOException {
        return super.createParser(content);
    }

    @Override
    public JsonParser createParser(byte[] content, int offset, int len) throws IOException {
        return super.createParser(content, offset, len);
    }

    @Override
    public JsonParser createParser(String content) throws IOException {
        return super.createParser(content);
    }

    @Override
    public JsonParser createParser(char[] content) throws IOException {
        return super.createParser(content);
    }

    @Override
    public JsonParser createParser(char[] content, int offset, int len) throws IOException {
        return super.createParser(content, offset, len);
    }

    @Override
    public JsonParser createParser(DataInput content) throws IOException {
        return super.createParser(content);
    }

    @Override
    public JsonParser createNonBlockingByteArrayParser() throws IOException {
        return super.createNonBlockingByteArrayParser();
    }

    @Override
    public SerializationConfig getSerializationConfig() {
        return super.getSerializationConfig();
    }

    @Override
    public DeserializationConfig getDeserializationConfig() {
        return super.getDeserializationConfig();
    }

    @Override
    public DeserializationContext getDeserializationContext() {
        return super.getDeserializationContext();
    }

    @Override
    public JSONObjectMapper setSerializerFactory(SerializerFactory f) {
        return (JSONObjectMapper)super.setSerializerFactory(f);
    }

    @Override
    public SerializerFactory getSerializerFactory() {
        return super.getSerializerFactory();
    }

    @Override
    public JSONObjectMapper setSerializerProvider(DefaultSerializerProvider p) {
        return (JSONObjectMapper)super.setSerializerProvider(p);
    }

    @Override
    public SerializerProvider getSerializerProvider() {
        return super.getSerializerProvider();
    }

    @Override
    public SerializerProvider getSerializerProviderInstance() {
        return super.getSerializerProviderInstance();
    }

    @Override
    public JSONObjectMapper setMixIns(Map<Class<?>, Class<?>> sourceMixins) {
        return (JSONObjectMapper)super.setMixIns(sourceMixins);
    }

    @Override
    public JSONObjectMapper addMixIn(Class<?> target, Class<?> mixinSource) {
        return (JSONObjectMapper)super.addMixIn(target, mixinSource);
    }

    @Override
    public JSONObjectMapper setMixInResolver(ClassIntrospector.MixInResolver resolver) {
        return (JSONObjectMapper)super.setMixInResolver(resolver);
    }

    @Override
    public Class<?> findMixInClassFor(Class<?> cls) {
        return super.findMixInClassFor(cls);
    }

    @Override
    public int mixInCount() {
        return super.mixInCount();
    }

    @Override
    public void setMixInAnnotations(Map<Class<?>, Class<?>> sourceMixins) {
        super.setMixInAnnotations(sourceMixins);
    }

    @Override
    public VisibilityChecker<?> getVisibilityChecker() {
        return super.getVisibilityChecker();
    }

    @Override
    public JSONObjectMapper setVisibility(VisibilityChecker<?> vc) {
        return (JSONObjectMapper)super.setVisibility(vc);
    }

    @Override
    public JSONObjectMapper setVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility) {
        return (JSONObjectMapper)super.setVisibility(forMethod, visibility);
    }

    @Override
    public SubtypeResolver getSubtypeResolver() {
        return super.getSubtypeResolver();
    }

    @Override
    public JSONObjectMapper setSubtypeResolver(SubtypeResolver str) {
        return (JSONObjectMapper)super.setSubtypeResolver(str);
    }

    @Override
    public JSONObjectMapper setAnnotationIntrospector(AnnotationIntrospector ai) {
        return (JSONObjectMapper)super.setAnnotationIntrospector(ai);
    }

    @Override
    public JSONObjectMapper setAnnotationIntrospectors(AnnotationIntrospector serializerAI, AnnotationIntrospector deserializerAI) {
        return (JSONObjectMapper)super.setAnnotationIntrospectors(serializerAI, deserializerAI);
    }

    @Override
    public JSONObjectMapper setPropertyNamingStrategy(PropertyNamingStrategy s) {
        return (JSONObjectMapper)super.setPropertyNamingStrategy(s);
    }

    @Override
    public PropertyNamingStrategy getPropertyNamingStrategy() {
        return super.getPropertyNamingStrategy();
    }

    @Override
    public JSONObjectMapper setAccessorNaming(AccessorNamingStrategy.Provider s) {
        return (JSONObjectMapper)super.setAccessorNaming(s);
    }

    @Override
    public JSONObjectMapper setDefaultPrettyPrinter(PrettyPrinter pp) {
        return (JSONObjectMapper)super.setDefaultPrettyPrinter(pp);
    }

    @Override
    public void setVisibilityChecker(VisibilityChecker<?> vc) {
        super.setVisibilityChecker(vc);
    }

    @Override
    public JSONObjectMapper setPolymorphicTypeValidator(PolymorphicTypeValidator ptv) {
        return (JSONObjectMapper)super.setPolymorphicTypeValidator(ptv);
    }

    @Override
    public PolymorphicTypeValidator getPolymorphicTypeValidator() {
        return super.getPolymorphicTypeValidator();
    }

    @Override
    public JSONObjectMapper setSerializationInclusion(JsonInclude.Include incl) {
        return (JSONObjectMapper)super.setSerializationInclusion(incl);
    }

    @Override
    public JSONObjectMapper setPropertyInclusion(JsonInclude.Value incl) {
        return (JSONObjectMapper)super.setPropertyInclusion(incl);
    }

    @Override
    public JSONObjectMapper setDefaultPropertyInclusion(JsonInclude.Value incl) {
        return (JSONObjectMapper)super.setDefaultPropertyInclusion(incl);
    }

    @Override
    public JSONObjectMapper setDefaultPropertyInclusion(JsonInclude.Include incl) {
        return (JSONObjectMapper)super.setDefaultPropertyInclusion(incl);
    }

    @Override
    public JSONObjectMapper setDefaultSetterInfo(JsonSetter.Value v) {
        return (JSONObjectMapper)super.setDefaultSetterInfo(v);
    }

    @Override
    public JSONObjectMapper setDefaultVisibility(JsonAutoDetect.Value vis) {
        return (JSONObjectMapper)super.setDefaultVisibility(vis);
    }

    @Override
    public JSONObjectMapper setDefaultMergeable(Boolean b) {
        return (JSONObjectMapper)super.setDefaultMergeable(b);
    }

    @Override
    public JSONObjectMapper setDefaultLeniency(Boolean b) {
        return (JSONObjectMapper)super.setDefaultLeniency(b);
    }

    @Override
    public void registerSubtypes(Class<?>... classes) {
        super.registerSubtypes(classes);
    }

    @Override
    public void registerSubtypes(NamedType... types) {
        super.registerSubtypes(types);
    }

    @Override
    public void registerSubtypes(Collection<Class<?>> subtypes) {
        super.registerSubtypes(subtypes);
    }

    @Override
    public JSONObjectMapper activateDefaultTyping(PolymorphicTypeValidator ptv) {
        return (JSONObjectMapper)super.activateDefaultTyping(ptv);
    }

    @Override
    public JSONObjectMapper activateDefaultTyping(PolymorphicTypeValidator ptv, DefaultTyping applicability) {
        return (JSONObjectMapper)super.activateDefaultTyping(ptv, applicability);
    }

    @Override
    public JSONObjectMapper activateDefaultTyping(PolymorphicTypeValidator ptv, DefaultTyping applicability, JsonTypeInfo.As includeAs) {
        return (JSONObjectMapper)super.activateDefaultTyping(ptv, applicability, includeAs);
    }

    @Override
    public JSONObjectMapper activateDefaultTypingAsProperty(PolymorphicTypeValidator ptv, DefaultTyping applicability, String propertyName) {
        return (JSONObjectMapper)super.activateDefaultTypingAsProperty(ptv, applicability, propertyName);
    }

    @Override
    public JSONObjectMapper deactivateDefaultTyping() {
        return (JSONObjectMapper)super.deactivateDefaultTyping();
    }

    @Override
    public JSONObjectMapper setDefaultTyping(TypeResolverBuilder<?> typer) {
        return (JSONObjectMapper)super.setDefaultTyping(typer);
    }

    @Override
    public JSONObjectMapper enableDefaultTyping() {
        return (JSONObjectMapper)super.enableDefaultTyping();
    }

    @Override
    public JSONObjectMapper enableDefaultTyping(DefaultTyping dti) {
        return (JSONObjectMapper)super.enableDefaultTyping(dti);
    }

    @Override
    public JSONObjectMapper enableDefaultTyping(DefaultTyping applicability, JsonTypeInfo.As includeAs) {
        return (JSONObjectMapper)super.enableDefaultTyping(applicability, includeAs);
    }

    @Override
    public JSONObjectMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName) {
        return (JSONObjectMapper)super.enableDefaultTypingAsProperty(applicability, propertyName);
    }

    @Override
    public JSONObjectMapper disableDefaultTyping() {
        return (JSONObjectMapper)super.disableDefaultTyping();
    }

    @Override
    public MutableConfigOverride configOverride(Class<?> type) {
        return super.configOverride(type);
    }

    @Override
    public MutableCoercionConfig coercionConfigDefaults() {
        return super.coercionConfigDefaults();
    }

    @Override
    public MutableCoercionConfig coercionConfigFor(LogicalType logicalType) {
        return super.coercionConfigFor(logicalType);
    }

    @Override
    public MutableCoercionConfig coercionConfigFor(Class<?> physicalType) {
        return super.coercionConfigFor(physicalType);
    }

    @Override
    public TypeFactory getTypeFactory() {
        return super.getTypeFactory();
    }

    @Override
    public JSONObjectMapper setTypeFactory(TypeFactory f) {
        return (JSONObjectMapper)super.setTypeFactory(f);
    }

    @Override
    public JavaType constructType(Type t) {
        return super.constructType(t);
    }

    @Override
    public JavaType constructType(TypeReference<?> typeRef) {
        return super.constructType(typeRef);
    }

    @Override
    public JsonNodeFactory getNodeFactory() {
        return super.getNodeFactory();
    }

    @Override
    public JSONObjectMapper setNodeFactory(JsonNodeFactory f) {
        return (JSONObjectMapper)super.setNodeFactory(f);
    }

    @Override
    public JSONObjectMapper setConstructorDetector(ConstructorDetector cd) {
        return (JSONObjectMapper)super.setConstructorDetector(cd);
    }

    @Override
    public JSONObjectMapper addHandler(DeserializationProblemHandler h) {
        return (JSONObjectMapper)super.addHandler(h);
    }

    @Override
    public JSONObjectMapper clearProblemHandlers() {
        return (JSONObjectMapper)super.clearProblemHandlers();
    }

    @Override
    public JSONObjectMapper setConfig(DeserializationConfig config) {
        return (JSONObjectMapper)super.setConfig(config);
    }

    @Override
    public void setFilters(FilterProvider filterProvider) {
        super.setFilters(filterProvider);
    }

    @Override
    public JSONObjectMapper setFilterProvider(FilterProvider filterProvider) {
        return (JSONObjectMapper)super.setFilterProvider(filterProvider);
    }

    @Override
    public JSONObjectMapper setBase64Variant(Base64Variant v) {
        return (JSONObjectMapper)super.setBase64Variant(v);
    }

    @Override
    public JSONObjectMapper setConfig(SerializationConfig config) {
        return (JSONObjectMapper)super.setConfig(config);
    }

    @Override
    public JsonFactory tokenStreamFactory() {
        return super.tokenStreamFactory();
    }

    @Override
    public JsonFactory getFactory() {
        return super.getFactory();
    }

    @Override
    public JSONObjectMapper setDateFormat(DateFormat dateFormat) {
        return (JSONObjectMapper)super.setDateFormat(dateFormat);
    }

    @Override
    public DateFormat getDateFormat() {
        return super.getDateFormat();
    }

    @Override
    public Object setHandlerInstantiator(HandlerInstantiator hi) {
        return super.setHandlerInstantiator(hi);
    }

    @Override
    public JSONObjectMapper setInjectableValues(InjectableValues injectableValues) {
        return (JSONObjectMapper)super.setInjectableValues(injectableValues);
    }

    @Override
    public InjectableValues getInjectableValues() {
        return super.getInjectableValues();
    }

    @Override
    public JSONObjectMapper setLocale(Locale l) {
        return (JSONObjectMapper)super.setLocale(l);
    }

    @Override
    public JSONObjectMapper setTimeZone(TimeZone tz) {
        return (JSONObjectMapper)super.setTimeZone(tz);
    }

    @Override
    public JSONObjectMapper setDefaultAttributes(ContextAttributes attrs) {
        return (JSONObjectMapper)super.setDefaultAttributes(attrs);
    }

    @Override
    public boolean isEnabled(MapperFeature f) {
        return super.isEnabled(f);
    }

    @Override
    public JSONObjectMapper configure(MapperFeature f, boolean state) {
        return (JSONObjectMapper)super.configure(f, state);
    }

    @Override
    public JSONObjectMapper enable(MapperFeature... f) {
        return (JSONObjectMapper)super.enable(f);
    }

    @Override
    public JSONObjectMapper disable(MapperFeature... f) {
        return (JSONObjectMapper)super.disable(f);
    }

    @Override
    public boolean isEnabled(SerializationFeature f) {
        return super.isEnabled(f);
    }

    @Override
    public JSONObjectMapper configure(SerializationFeature f, boolean state) {
        return (JSONObjectMapper)super.configure(f, state);
    }

    @Override
    public JSONObjectMapper enable(SerializationFeature f) {
        return (JSONObjectMapper)super.enable(f);
    }

    @Override
    public JSONObjectMapper enable(SerializationFeature first, SerializationFeature... f) {
        return (JSONObjectMapper)super.enable(first, f);
    }

    @Override
    public JSONObjectMapper disable(SerializationFeature f) {
        return (JSONObjectMapper)super.disable(f);
    }

    @Override
    public JSONObjectMapper disable(SerializationFeature first, SerializationFeature... f) {
        return (JSONObjectMapper)super.disable(first, f);
    }

    @Override
    public boolean isEnabled(DeserializationFeature f) {
        return super.isEnabled(f);
    }

    @Override
    public JSONObjectMapper configure(DeserializationFeature f, boolean state) {
        return (JSONObjectMapper)super.configure(f, state);
    }

    @Override
    public JSONObjectMapper enable(DeserializationFeature feature) {
        return (JSONObjectMapper)super.enable(feature);
    }

    @Override
    public JSONObjectMapper enable(DeserializationFeature first, DeserializationFeature... f) {
        return (JSONObjectMapper)super.enable(first, f);
    }

    @Override
    public JSONObjectMapper disable(DeserializationFeature feature) {
        return (JSONObjectMapper)super.disable(feature);
    }

    @Override
    public JSONObjectMapper disable(DeserializationFeature first, DeserializationFeature... f) {
        return (JSONObjectMapper)super.disable(first, f);
    }

    @Override
    public boolean isEnabled(JsonParser.Feature f) {
        return super.isEnabled(f);
    }

    @Override
    public JSONObjectMapper configure(JsonParser.Feature f, boolean state) {
        return (JSONObjectMapper)super.configure(f, state);
    }

    @Override
    public JSONObjectMapper enable(JsonParser.Feature... features) {
        return (JSONObjectMapper)super.enable(features);
    }

    @Override
    public JSONObjectMapper disable(JsonParser.Feature... features) {
        return (JSONObjectMapper)super.disable(features);
    }

    @Override
    public boolean isEnabled(JsonGenerator.Feature f) {
        return super.isEnabled(f);
    }

    @Override
    public JSONObjectMapper configure(JsonGenerator.Feature f, boolean state) {
        return (JSONObjectMapper)super.configure(f, state);
    }

    @Override
    public JSONObjectMapper enable(JsonGenerator.Feature... features) {
        return (JSONObjectMapper)super.enable(features);
    }

    @Override
    public JSONObjectMapper disable(JsonGenerator.Feature... features) {
        return (JSONObjectMapper)super.disable(features);
    }

    @Override
    public boolean isEnabled(JsonFactory.Feature f) {
        return super.isEnabled(f);
    }

    @Override
    public boolean isEnabled(StreamReadFeature f) {
        return super.isEnabled(f);
    }

    @Override
    public boolean isEnabled(StreamWriteFeature f) {
        return super.isEnabled(f);
    }

    @Override
    public <T> T readValue(JsonParser p, Class<T> valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(p, valueType);
    }

    @Override
    public <T> T readValue(JsonParser p, TypeReference<T> valueTypeRef) throws IOException, StreamReadException, DatabindException {
        return super.readValue(p, valueTypeRef);
    }

    @Override
    public <T> T readValue(JsonParser p, JavaType valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(p, valueType);
    }

    @Override
    public <T extends TreeNode> T readTree(JsonParser p) throws IOException {
        return super.readTree(p);
    }

    @Override
    public <T> MappingIterator<T> readValues(JsonParser p, ResolvedType valueType) throws IOException {
        return super.readValues(p, valueType);
    }

    @Override
    public <T> MappingIterator<T> readValues(JsonParser p, JavaType valueType) throws IOException {
        return super.readValues(p, valueType);
    }

    @Override
    public <T> MappingIterator<T> readValues(JsonParser p, Class<T> valueType) throws IOException {
        return super.readValues(p, valueType);
    }

    @Override
    public <T> MappingIterator<T> readValues(JsonParser p, TypeReference<T> valueTypeRef) throws IOException {
        return super.readValues(p, valueTypeRef);
    }

    @Override
    public JsonNode readTree(InputStream in) throws IOException {
        return super.readTree(in);
    }

    @Override
    public JsonNode readTree(Reader r) throws IOException {
        return super.readTree(r);
    }

    @Override
    public JsonNode readTree(String content) throws JsonProcessingException, JsonMappingException {
        return super.readTree(content);
    }

    @Override
    public JsonNode readTree(byte[] content) throws IOException {
        return super.readTree(content);
    }

    @Override
    public JsonNode readTree(byte[] content, int offset, int len) throws IOException {
        return super.readTree(content, offset, len);
    }

    @Override
    public JsonNode readTree(File file) throws IOException {
        return super.readTree(file);
    }

    @Override
    public JsonNode readTree(URL source) throws IOException {
        return super.readTree(source);
    }

    @Override
    public void writeValue(JsonGenerator g, Object value) throws IOException, StreamWriteException, DatabindException {
        super.writeValue(g, value);
    }

    @Override
    public void writeTree(JsonGenerator g, TreeNode rootNode) throws IOException {
        super.writeTree(g, rootNode);
    }

    @Override
    public void writeTree(JsonGenerator g, JsonNode rootNode) throws IOException {
        super.writeTree(g, rootNode);
    }

    @Override
    public ObjectNode createObjectNode() {
        return super.createObjectNode();
    }

    @Override
    public ArrayNode createArrayNode() {
        return super.createArrayNode();
    }

    @Override
    public JsonNode missingNode() {
        return super.missingNode();
    }

    @Override
    public JsonNode nullNode() {
        return super.nullNode();
    }

    @Override
    public JsonParser treeAsTokens(TreeNode n) {
        return super.treeAsTokens(n);
    }

    @Override
    public <T> T treeToValue(TreeNode n, Class<T> valueType) throws IllegalArgumentException, JsonProcessingException {
        return super.treeToValue(n, valueType);
    }

    @Override
    public <T> T treeToValue(TreeNode n, JavaType valueType) throws IllegalArgumentException, JsonProcessingException {
        return super.treeToValue(n, valueType);
    }

    @Override
    public <T extends JsonNode> T valueToTree(Object fromValue) throws IllegalArgumentException {
        return super.valueToTree(fromValue);
    }

    @Override
    public boolean canSerialize(Class<?> type) {
        return super.canSerialize(type);
    }

    @Override
    public boolean canSerialize(Class<?> type, AtomicReference<Throwable> cause) {
        return super.canSerialize(type, cause);
    }

    @Override
    public boolean canDeserialize(JavaType type) {
        return super.canDeserialize(type);
    }

    @Override
    public boolean canDeserialize(JavaType type, AtomicReference<Throwable> cause) {
        return super.canDeserialize(type, cause);
    }

    @Override
    public <T> T readValue(File src, Class<T> valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueType);
    }

    @Override
    public <T> T readValue(File src, TypeReference<T> valueTypeRef) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueTypeRef);
    }

    @Override
    public <T> T readValue(File src, JavaType valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueType);
    }

    @Override
    public <T> T readValue(URL src, Class<T> valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueType);
    }

    @Override
    public <T> T readValue(URL src, TypeReference<T> valueTypeRef) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueTypeRef);
    }

    @Override
    public <T> T readValue(URL src, JavaType valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueType);
    }

    @Override
    public <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException, JsonMappingException {
        return super.readValue(content, valueType);
    }

    @Override
    public <T> T readValue(String content, TypeReference<T> valueTypeRef) throws JsonProcessingException, JsonMappingException {
        return super.readValue(content, valueTypeRef);
    }

    @Override
    public <T> T readValue(String content, JavaType valueType) throws JsonProcessingException, JsonMappingException {
        return super.readValue(content, valueType);
    }

    @Override
    public <T> T readValue(Reader src, Class<T> valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueType);
    }

    @Override
    public <T> T readValue(Reader src, TypeReference<T> valueTypeRef) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueTypeRef);
    }

    @Override
    public <T> T readValue(Reader src, JavaType valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueType);
    }

    @Override
    public <T> T readValue(InputStream src, Class<T> valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueType);
    }

    @Override
    public <T> T readValue(InputStream src, TypeReference<T> valueTypeRef) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueTypeRef);
    }

    @Override
    public <T> T readValue(InputStream src, JavaType valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueType);
    }

    @Override
    public <T> T readValue(byte[] src, Class<T> valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueType);
    }

    @Override
    public <T> T readValue(byte[] src, int offset, int len, Class<T> valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, offset, len, valueType);
    }

    @Override
    public <T> T readValue(byte[] src, TypeReference<T> valueTypeRef) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueTypeRef);
    }

    @Override
    public <T> T readValue(byte[] src, int offset, int len, TypeReference<T> valueTypeRef) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, offset, len, valueTypeRef);
    }

    @Override
    public <T> T readValue(byte[] src, JavaType valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, valueType);
    }

    @Override
    public <T> T readValue(byte[] src, int offset, int len, JavaType valueType) throws IOException, StreamReadException, DatabindException {
        return super.readValue(src, offset, len, valueType);
    }

    @Override
    public <T> T readValue(DataInput src, Class<T> valueType) throws IOException {
        return super.readValue(src, valueType);
    }

    @Override
    public <T> T readValue(DataInput src, JavaType valueType) throws IOException {
        return super.readValue(src, valueType);
    }

    @Override
    public void writeValue(File resultFile, Object value) throws IOException, StreamWriteException, DatabindException {
        super.writeValue(resultFile, value);
    }

    @Override
    public void writeValue(OutputStream out, Object value) throws IOException, StreamWriteException, DatabindException {
        super.writeValue(out, value);
    }

    @Override
    public void writeValue(DataOutput out, Object value) throws IOException {
        super.writeValue(out, value);
    }

    @Override
    public void writeValue(Writer w, Object value) throws IOException, StreamWriteException, DatabindException {
        super.writeValue(w, value);
    }

    @Override
    public String writeValueAsString(Object value) throws JsonProcessingException {
        return super.writeValueAsString(value);
    }

    @Override
    public byte[] writeValueAsBytes(Object value) throws JsonProcessingException {
        return super.writeValueAsBytes(value);
    }

    @Override
    public ObjectWriter writer() {
        return super.writer();
    }

    @Override
    public ObjectWriter writer(SerializationFeature feature) {
        return super.writer(feature);
    }

    @Override
    public ObjectWriter writer(SerializationFeature first, SerializationFeature... other) {
        return super.writer(first, other);
    }

    @Override
    public ObjectWriter writer(DateFormat df) {
        return super.writer(df);
    }

    @Override
    public ObjectWriter writerWithView(Class<?> serializationView) {
        return super.writerWithView(serializationView);
    }

    @Override
    public ObjectWriter writerFor(Class<?> rootType) {
        return super.writerFor(rootType);
    }

    @Override
    public ObjectWriter writerFor(TypeReference<?> rootType) {
        return super.writerFor(rootType);
    }

    @Override
    public ObjectWriter writerFor(JavaType rootType) {
        return super.writerFor(rootType);
    }

    @Override
    public ObjectWriter writer(PrettyPrinter pp) {
        return super.writer(pp);
    }

    @Override
    public ObjectWriter writerWithDefaultPrettyPrinter() {
        return super.writerWithDefaultPrettyPrinter();
    }

    @Override
    public ObjectWriter writer(FilterProvider filterProvider) {
        return super.writer(filterProvider);
    }

    @Override
    public ObjectWriter writer(FormatSchema schema) {
        return super.writer(schema);
    }

    @Override
    public ObjectWriter writer(Base64Variant defaultBase64) {
        return super.writer(defaultBase64);
    }

    @Override
    public ObjectWriter writer(CharacterEscapes escapes) {
        return super.writer(escapes);
    }

    @Override
    public ObjectWriter writer(ContextAttributes attrs) {
        return super.writer(attrs);
    }

    @Override
    public ObjectWriter writerWithType(Class<?> rootType) {
        return super.writerWithType(rootType);
    }

    @Override
    public ObjectWriter writerWithType(TypeReference<?> rootType) {
        return super.writerWithType(rootType);
    }

    @Override
    public ObjectWriter writerWithType(JavaType rootType) {
        return super.writerWithType(rootType);
    }

    @Override
    public ObjectReader reader() {
        return super.reader();
    }

    @Override
    public ObjectReader reader(DeserializationFeature feature) {
        return super.reader(feature);
    }

    @Override
    public ObjectReader reader(DeserializationFeature first, DeserializationFeature... other) {
        return super.reader(first, other);
    }

    @Override
    public ObjectReader readerForUpdating(Object valueToUpdate) {
        return super.readerForUpdating(valueToUpdate);
    }

    @Override
    public ObjectReader readerFor(JavaType type) {
        return super.readerFor(type);
    }

    @Override
    public ObjectReader readerFor(Class<?> type) {
        return super.readerFor(type);
    }

    @Override
    public ObjectReader readerFor(TypeReference<?> type) {
        return super.readerFor(type);
    }

    @Override
    public ObjectReader readerForArrayOf(Class<?> type) {
        return super.readerForArrayOf(type);
    }

    @Override
    public ObjectReader readerForListOf(Class<?> type) {
        return super.readerForListOf(type);
    }

    @Override
    public ObjectReader readerForMapOf(Class<?> type) {
        return super.readerForMapOf(type);
    }

    @Override
    public ObjectReader reader(JsonNodeFactory f) {
        return super.reader(f);
    }

    @Override
    public ObjectReader reader(FormatSchema schema) {
        return super.reader(schema);
    }

    @Override
    public ObjectReader reader(InjectableValues injectableValues) {
        return super.reader(injectableValues);
    }

    @Override
    public ObjectReader readerWithView(Class<?> view) {
        return super.readerWithView(view);
    }

    @Override
    public ObjectReader reader(Base64Variant defaultBase64) {
        return super.reader(defaultBase64);
    }

    @Override
    public ObjectReader reader(ContextAttributes attrs) {
        return super.reader(attrs);
    }

    @Override
    public ObjectReader reader(JavaType type) {
        return super.reader(type);
    }

    @Override
    public ObjectReader reader(Class<?> type) {
        return super.reader(type);
    }

    @Override
    public ObjectReader reader(TypeReference<?> type) {
        return super.reader(type);
    }

    @Override
    public <T> T convertValue(Object fromValue, Class<T> toValueType) throws IllegalArgumentException {
        return super.convertValue(fromValue, toValueType);
    }

    @Override
    public <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) throws IllegalArgumentException {
        return super.convertValue(fromValue, toValueTypeRef);
    }

    @Override
    public <T> T convertValue(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
        return super.convertValue(fromValue, toValueType);
    }

    @Override
    protected Object _convert(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
        return super._convert(fromValue, toValueType);
    }

    @Override
    public <T> T updateValue(T valueToUpdate, Object overrides) throws JsonMappingException {
        return super.updateValue(valueToUpdate, overrides);
    }

    @Override
    public JsonSchema generateJsonSchema(Class<?> t) throws JsonMappingException {
        return super.generateJsonSchema(t);
    }

    @Override
    public void acceptJsonFormatVisitor(Class<?> type, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
        super.acceptJsonFormatVisitor(type, visitor);
    }

    @Override
    public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
        super.acceptJsonFormatVisitor(type, visitor);
    }

    @Override
    protected TypeResolverBuilder<?> _constructDefaultTypeResolverBuilder(DefaultTyping applicability, PolymorphicTypeValidator ptv) {
        return super._constructDefaultTypeResolverBuilder(applicability, ptv);
    }

    @Override
    protected DefaultSerializerProvider _serializerProvider(SerializationConfig config) {
        return super._serializerProvider(config);
    }

    @Override
    protected Object _readValue(DeserializationConfig cfg, JsonParser p, JavaType valueType) throws IOException {
        return super._readValue(cfg, p, valueType);
    }

    @Override
    protected Object _readMapAndClose(JsonParser p0, JavaType valueType) throws IOException {
        return super._readMapAndClose(p0, valueType);
    }

    @Override
    protected JsonNode _readTreeAndClose(JsonParser p0) throws IOException {
        return super._readTreeAndClose(p0);
    }

    @Override
    protected DefaultDeserializationContext createDeserializationContext(JsonParser p, DeserializationConfig cfg) {
        return super.createDeserializationContext(p, cfg);
    }

    @Override
    protected JsonToken _initForReading(JsonParser p, JavaType targetType) throws IOException {
        return super._initForReading(p, targetType);
    }

    @Override
    protected JsonDeserializer<Object> _findRootDeserializer(DeserializationContext ctxt, JavaType valueType) throws DatabindException {
        return super._findRootDeserializer(ctxt, valueType);
    }

    @Override
    protected void _verifySchemaType(FormatSchema schema) {
        super._verifySchemaType(schema);
    }

    public static JSONObjectMapper build(){
        return getDefualtObjectMapper();
    }

    /**
     * 默认配置
     * @return
     */
    public static JSONObjectMapper getDefualtObjectMapper(){
        JSONObjectMapper mapper = new JSONObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //关闭日期序列化为时间戳的功能
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //关闭序列化的时候没有为属性找到getter方法,报错
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //关闭反序列化的时候，没有找到属性的setter报错
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //在反序列化时忽略在 json 中存在但 Java 对象不存在的属性,反序列化的时候如果多了其他属性,不抛出异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        //如果是空对象的时候,不抛异常
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //在序列化时日期格式默认为 yyyy-MM-dd'T'HH:mm:ss.SSSZ
        //mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //序列化的时候序列对象的所有属性
        //mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //在序列化时忽略值为 null 的属性
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //忽略值为默认值的属性
//        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
        //允许使用未带引号的字段名
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //允许使用单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //该特性决定parser将是否允许解析使用Java/C++ 样式的注释（包括'/'+'*' 和'//' 变量）。 由于JSON标准说明书上面没有提到注释是否是合法的组成，所以这是一个非标准的特性；
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        //该特性决定parser是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 如果该属性关闭，则如果遇到这些字符，则会抛出异常。
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        SimpleModule simpleModule = new SimpleModule();
        //json值序列化
        simpleModule.addSerializer(Date.class, DateTimeDefaultSerializer.INSTANCE);
        //json值反序列化
        simpleModule.addDeserializer(Date.class, DataTimeDefaultDeserializer.INSTANCE);
        mapper.registerModule(simpleModule);
        return mapper;
    }

    public static <O> String toJsonString(O obj) {
        JSONObjectMapper mapper = getDefualtObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("{}",e);
        }
        return null;
    }

    public static <T> T parseObject(String jsonStr, TypeReference<T> type) {
        JSONObjectMapper mapper = getDefualtObjectMapper();
        try {
            return mapper.readValue(jsonStr, type);
        } catch (JsonProcessingException ex) {
            LOGGER.error("对象反序列化异常，{}", ex);
        }
        return null;
    }

    public static <T> T parseObject(String jsonStr, Class<T> type) {
        JSONObjectMapper mapper = getDefualtObjectMapper();
        try {
            return mapper.readValue(jsonStr, type);
        } catch (JsonProcessingException ex) {
            LOGGER.error("对象反序列化异常，{}", ex);
        }
        return null;
    }
}
