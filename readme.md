QuickDto
=====

What
=====

DTO code generation using APT, that handles the 
boilerplate code needed for dirty field detection, 
transfer to and from the DTO, and equals/hashCode 
implementations.

By using Java APT (Annotation Preprocessing Tool), 
QuickDto is able to create a DTO that performs better
then other libraries that depend on Reflections which
can be slow.

Setup
=====

QuickDto can be found in maven central here:

Here are a list of links to setup APT:
* [Intellij IDEA](https://www.jetbrains.com/help/idea/2017.1/configuring-annotation-processing.html)
* [Eclipse](http://help.eclipse.org/kepler/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Fguide%2Fjdt_apt_getting_started.htm)
* [Gradle](https://github.com/tbroyer/gradle-apt-plugin)
* [Maven](https://github.com/bsorrentino/maven-annotation-plugin)

How
=======

QuickDto uses `DtoDef` objects to define the properties
of the DTO.  APT is then used to generate the actual DTO 
base on the definition.

```
@QuickDto(source = User.class)
public class UserDtoDef {
	@EqualsHashCode
	@CopyFromOnly
	long id;
	String firstName;
	String lastName;
}
```

generates:
```
public class UserDto {

	public static enum Fields {
		ID("id"),
		FIRST_NAME("firstName"),
		LAST_NAME("lastName");

		private String name;

		Fields(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}

		public static Fields get(String fieldName) {
			for (Fields field: Fields.values()) {
				if (field.name.equals(fieldName)) {
					return field;
				}
			}
			return null;
		}
	}

	private long id;
	private java.lang.String firstName;
	private java.lang.String lastName;

	protected Set<Fields> dirtyFields = new HashSet<Fields>();

	public void setDirty(Fields field, boolean dirty) {
		if (dirty) {
			dirtyFields.add(field);
		} else {
			dirtyFields.remove(field);
		}
	}

	public boolean isDirty() {
		return !dirtyFields.isEmpty();
	}

	public boolean isDirty(Fields field) {
		return dirtyFields.contains(field);
	}

	public void setDirtyFields(Set<Fields> dirtyFields) { 
		this.dirtyFields = dirtyFields;
	}

	public Set<Fields> getDirtyFields() {
		return dirtyFields;
	}

	public void resetDirty() {
		dirtyFields.clear();
	}

	public long getId() {
		return id;
	}

	public java.lang.String getFirstName() {
		return firstName;
	}

	public void setFirstName(java.lang.String firstName) {
		if (!Objects.equals(this.firstName, firstName)) {
			setDirty(Fields.FIRST_NAME, true);
			this.firstName = firstName;
		}
	}

	public java.lang.String getLastName() {
		return lastName;
	}

	public void setLastName(java.lang.String lastName) {
		if (!Objects.equals(this.lastName, lastName)) {
			setDirty(Fields.LAST_NAME, true);
			this.lastName = lastName;
		}
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		UserDto that = (UserDto) o;

		if (id != that.id) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		long temp;

		int result = (int) (id ^ (id >>> 32));
		return result;
	}

	@GwtIncompatible
	public void copyTo(com.github.quickdto.testharness.impl.User dest, Fields... fields) {
		if (fields.length > 0) {
			copyTo(dest, Arrays.asList(fields));
		} else {
			copyTo(dest, getDirtyFields());
		}
	}

	@GwtIncompatible
	public void copyTo(com.github.quickdto.testharness.impl.User dest, Iterable<Fields> fields) {
		for (Fields field: fields) {
			switch (field) {
				case FIRST_NAME:
					dest.setFirstName(firstName);
					break;
				case LAST_NAME:
					dest.setLastName(lastName);
					break;
			}
		}
	}

	@GwtIncompatible
	public void copyFrom(com.github.quickdto.testharness.impl.User source) {
		firstName = source.getFirstName();
		id = source.getId();
		lastName = source.getLastName();
	}

}
```

Annotations
=====

@QuickDto
--------
Used on the class to signify that it is a DtoDef, and is required for APT processing.

* `extend` - defines the class the DTO should extend.
* `implement` - defines the interfaces the DTO should implement.
* `source` - defines the source class should be used in the copy methods.
* `strictCopy` [true] - defines whether the QuickDto should enforce that all the fields in the
DTO are copied to at least one source.  If not copied, then a compile error is created.  This 
is beneficial for ensuring that name refactoring doesn't break copy methods.  
* `fieldAnnotationsOnGetter` [false] - defines whether non QuickDto annotations on fields 
should be placed on the property or the getter method.
* `copyMethods` [false] - defines whether QuickDto should copy methods defined in the DtoDef
into the DTO class.  This functionality uses JDK classes that are not guaranteed to exist in 
all JDKs, so it isn't included by default. This is a compile time dependency, so if the copy 
is successful, then it can be used on any JVM.

@EqualsHashCode
---------
Used on fields that should be included in the `equals` and `hashCode` methods.  Only fields
with this annotation will be included, but if no field have the annotation, then all the 
fields are included in the `equals` and `hashCode` methods.

@CopyToOnly
-------
Used on fields that should only be copied from the source to the DTO.  This
is useful for ID fields that are created on the server and need to be sent out.

* `getter` [true] - defines whether the getter method should be generated in the DTO.

@CopyFromOnly
----------
Used on fields that should only be copied from the DTO to the source.  This
is useful for password fields that are sent in, but never sent out.

* `setter` [false] - defines whether the setter method should be generated in the DTO.

@StrictCopy
----------
Used on fields to enforce or not enforce strict copy.

*`value` - true if the field should enforce, or false if the field shouldn't be enforced.

FAQ
=======

Q: What is the @GwtIncompatible annotation for?
---------
A: This project was developed for use in GWT (Google Web Toolkit)
projects, which compile Java to Javascript.  The annotation 
is used to tell GWT not to transform those methods.  QuickDto
owns the annotation, so there isn't any dependency on GWT.

Q: Does QuickDto support conversions for types when copying?
---------
A: Currently QuickDto doesn't support conversions.  It is the next
feature planned for implementation.



