package petclinic.vets;

import petclinic.util.*;

public final class VetData extends TestDatabase
{
   public Vet create(String fullName, String... specialtyNames)
   {
      String[] names = fullName.split(" ");

      Vet vet = new Vet();
      vet.setFirstName(names[0]);
      vet.setLastName(names[names.length - 1]);

      for (String specialtyName : specialtyNames) {
         Specialty specialty = new Specialty();
         specialty.setName(specialtyName);

         vet.getSpecialties().add(specialty);
      }

      db.save(vet);
      return vet;
   }
}
