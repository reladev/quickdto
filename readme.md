QuickDto and QuickCopy
=====

What
=====

QuickDto was originally created to make it easy to create DTOs that could 
handle partial data through dirty checking.  It was then expanded to provide
`equals` and `hashCode`, and
"copy" methods, that performed better than reflection based libraries. 
Finally, QuickCopy was added to allow copying of any object to another object.

QuickDto/QuickCopy are Java Annotation Preprocessors that generate source
code at compile time.  The generated code is similar to what would be written
by hand, and can be easily accessed and used by the debugger to set through
what is happening.

QuickDto/QuickCopy allow converter methods to be added easily convert from
one type to another.  It also understands if the coping classes are a QuickDto
or use QuickCopy to automatically handle conversions even if they are a List or
Set.

The dirty checking has been moved to a Feature, which means that the dirty
checking only needs to be there if desired.  The Feature construct also allows
for other custom extensions to inject logic into generated DTOs and even inside
the accessor methods of the DTO.

Setup
=====

QuickDto can be found in maven central here:
https://mvnrepository.com/artifact/org.reladev.quickdto/quickdto

Here are a list of links to setup APT:
* [Intellij IDEA](https://www.jetbrains.com/help/idea/2017.1/configuring-annotation-processing.html)
* [Eclipse](http://help.eclipse.org/kepler/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Fguide%2Fjdt_apt_getting_started.htm)
* [Gradle](https://github.com/tbroyer/gradle-apt-plugin)
* [Maven](https://github.com/bsorrentino/maven-annotation-plugin)

How
=======

##Create DTO
QuickDto generates DTOs based on a definition object.  Create POJO with the
DTO name plus the suffix "Def".  Add @QuickDto to the class, and fill the 
POJO with fields.  Modifiers don't matter, and no need to generate getters or
setters (that is what QuickDto is for).

QuickDto will generate a new file with the same package and name minus "Def".
If `sources` were added to the QuickDto annotation, then copy methods will
also be generated.  To add dirty checking, just add `DirtyFeature.class` to 
the QuickDto `features`. 

```
@QuickDto(source = User.class, features = DirtyFeature.class)
public class UserDtoDef {
	@EqualsHashCode
	@ExcludeCopyFrom
	long id;
	String firstName;
	String lastName;
}
```
##Create a CopyUtil
To create a CopyUtil, just add the QuickCopy annotation to any class, and provide
target classes that QuickCopy should copy to and from.  QuickCopy will generate a 
new class with the same package and name with "CopyUtil" append.  QuickCopy will use the
getters and setters to perform the copy if they exist or it will go directly if the 
fields are `public`.

```
@QuickCopy(source = UserDto.class)
public class User {
	public long id;
	public String firstName;
	private String lastName;
	
	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
}
```

#Annotations

##@QuickDto
Used on the class to signify that it is a DtoDef, and is required for APT processing.

* `extend` - defines the class the DTO should extend.
* `implement` - defines the interfaces the DTO should implement.
* `sources` - defines the source class should be used in the copy methods.
* `converters` - 
* `features` -
* `fieldAnnotationsOnGetter` [false] - defines whether non QuickDto annotations on fields 
should be placed on the property or the getter method.
* `copyMethods` [false] - defines whether QuickDto should copy methods defined in the DtoDef
into the DTO class.  This functionality uses JDK classes that are not guaranteed to exist in 
all JDKs, so it isn't included by default. This is a compile time dependency, so if the copy 
is successful, then it can be used on any JVM.

##@QuickCopy
Used on the class to signify that a CopyUtil should be created to help coping to the specified targets.

* `targets` - 
* `converters` - 

##@QuickDtoConfiguration
Used on the class to signify that a CopyUtil should be created to help coping to the specified targets.

* `quickDtoDefSuffix` ["Def"] - The suffix for all QuickDto Definitions.  The suffix will be removed 
to for the Dto's name.
* `quickCopySuffix` ["CopyUtil"]  - The suffix that will be added to the annotated class's name to 
form the QuickCopy class.
* `globalFeatures` - Features that should be included on every QuickDto/QuickCopy.

##@EqualsHashCode
Used on fields that should be included in the `equals` and `hashCode` methods.  Only fields
with this annotation will be included, but if no field have the annotation, then all the 
fields are included in the `equals` and `hashCode` methods.

##@ExcludeCopy
Used on fields that should not be included in copy methods.

* `values` - Specifies which Classes the exclude should apply to.  If blank then it applies to all classes.

##@ExcludeCopyFrom
Used on fields that should not be included in copy methods that copy this field to another field.

* `values` - Specifies which Classes the exclude should apply to.  If blank then it applies to all classes.

##@ExcludeCopyTo
Used on fields that should not be included in copy methods that copy another field into this field.

* `values` - Specifies which Classes the exclude should apply to.  If blank then it applies to all classes.

#Features
QuickDto allows for extensions to be made to add logic to DTOs.  To create a feature, just extend 
QuickDtoFeature and implement the methods to add logic to DTO getter, setters, or extra methods.  To use 
a feature, just add the feature class to any @QuickDto, or the @QuickDtoConfiguration to apply to the 
feature to all DTOs.

##DirtyFeature
This feature adds dirty tracking to DTOs. If a field changes, it is marked dirty.  Additional methods are 
added to provide checking if overall object is dirty or if specific fields are dirty, marking/resetting 
fields as dirty, and copy methods to only copy fields that are dirty.

The dirty feature is create for handling partial objects that are sent in Restful APIs that use PATCH.
 
###Methods
TBD

FAQ
=======

Q: Do I use the DTO or the DTO Def when defining other DTOs?
---------
A: Either one can be used.  Using the DTO might show errors until the DTO is generated
on the next compile.

Q: What is the @GwtIncompatible annotation for?
---------
A: This project was developed for use in GWT (Google Web Toolkit)
projects, which compile Java to Javascript.  The annotation 
is used to tell GWT not to transform those methods.  QuickDto
owns the annotation, so there isn't any dependency on GWT.



