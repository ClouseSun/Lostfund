package cn.bit.model;

import cn.bit.file.AbstractFileTree;

/**
 * Created by zhehua on 29/03/2017.
 */
public class IteProject {
   AbstractFileTree projectTree;

   public IteProject(AbstractFileTree projectTree) {
      this.projectTree = projectTree;
   }

   public AbstractFileTree getProjectTree() {
      return projectTree;
   }
}